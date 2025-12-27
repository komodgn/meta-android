package com.example.metasearch.core.data.impl.repository

import android.content.Context
import android.util.Base64.decode
import android.util.Log
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.metasearch.core.data.api.repository.DatabaseNameRepository
import com.example.metasearch.core.data.api.repository.GalleryRepository
import com.example.metasearch.core.data.api.repository.ImageAnalysisRepository
import com.example.metasearch.core.data.api.repository.PersonRepository
import com.example.metasearch.core.network.request.ChangeNameRequest
import com.example.metasearch.core.network.service.AIService
import com.example.metasearch.core.network.service.WebService
import com.example.metasearch.core.room.api.dao.AnalyzedImageDao
import com.example.metasearch.core.room.api.entity.AnalyzedImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ImageAnalysisRepositoryImpl @Inject constructor(
    private val analyzedImageDao: AnalyzedImageDao,
    private val galleryRepository: GalleryRepository,
    private val databaseNameRepository: DatabaseNameRepository,
    private val personRepository: PersonRepository,
    private val aiService: AIService,
    private val webService: WebService,
) : ImageAnalysisRepository {
    private val tag = "ImageAnalysisRepo"
    private val chunkSize = 10

    override fun getAnalysisStatus(context: Context): Flow<Boolean> {
        return WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow("ImageAnalysisWork")
            .map { workInfos ->
                workInfos.any { it.state == WorkInfo.State.RUNNING }
            }
    }

    override suspend fun runFullAnalysis() = withContext(Dispatchers.IO) {
        Log.d(tag, "runFullAnalysis 함수 실행")

        val currentGalleryPaths = galleryRepository.getAllGalleryPaths()
        val alreadyAnalyzedPaths = analyzedImageDao.getAllAnalyzedPaths()
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        deleteMissingImages(alreadyAnalyzedPaths, currentGalleryPaths, dbName)

        val addPaths = currentGalleryPaths.filter { path ->
            path !in alreadyAnalyzedPaths && isImageFile(path)
        }

        if (addPaths.isNotEmpty()) {
            addPaths.chunked(chunkSize).forEachIndexed { index, chunk ->
                uploadImageChunk(index, chunk, dbName)
            }
        }

        syncMismatchedNames(dbName)
    }

    private suspend fun deleteMissingImages(alreadyPaths: List<String>, currentPaths: List<String>, dbName: String) {
        val deletePaths = alreadyPaths.filter { it !in currentPaths }
        deletePaths.forEach { path ->
            val fileNamePart = createMultipartBodyPartFromFilePath("deleteImage", path)
            val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

            val webSuccess = runCatching { webService.uploadWebDeleteImage(fileNamePart, dbName) }.isSuccess
            val aiSuccess = runCatching { aiService.uploadDeleteImage(fileNamePart, dbNameBody) }.isSuccess

            if (webSuccess && aiSuccess) analyzedImageDao.deletePath(path)
        }
    }

    private suspend fun uploadImageChunk(index: Int, chunk: List<String>, dbName: String) {
        Log.d(tag, "청크 처리 중: ${index + 1}번째 (${chunk.size}개)")
        val successfulPathsInChunk = mutableListOf<String>()
        var isAllSuccess = true

        for (path in chunk) {
            val isSuccess = uploadSingleImage(path, dbName)
            if (isSuccess) {
                successfulPathsInChunk.add(path)
            } else {
                isAllSuccess = false
                Log.e(tag, "업로드 실패로 인한 청크 중단: $path")
                break
            }
        }

        if (isAllSuccess) {
            processAnalysisFinish(index, successfulPathsInChunk, dbName)
        }
    }

    private suspend fun uploadSingleImage(path: String, dbName: String): Boolean {
        val webImagePart = createMultipartBodyPartFromFilePath("image", path)
        val aiImagePart = createMultipartBodyPartFromFilePath("addImage", path)
        val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

        runCatching { webService.uploadWebAddImage(webImagePart, dbName) }
        val aiResult = runCatching { aiService.uploadAddImage(aiImagePart, dbNameBody) }

        return aiResult.isSuccess
    }

    private suspend fun processAnalysisFinish(index: Int, successfulPaths: List<String>, dbName: String) {
        val personCount = personRepository.getPersonCount()
        val finishBody = "true".toRequestBody("text/plain".toMediaTypeOrNull())
        val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())
        val countBody = personCount.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        runCatching { aiService.uploadFinish(finishBody, dbNameBody, countBody) }
            .onSuccess { response ->
                Log.d(tag, "서버로부터 수신한 인물 수: ${response.images.size}")

                response.images.forEach { person ->
                    runCatching {
                        if (person.isFaceExit && person.imageName != null && person.imageBytes != null) {
                            val decodedBytes = decode(person.imageBytes, android.util.Base64.DEFAULT)
                            personRepository.addAnalyzedPerson(person.imageName!!, decodedBytes)
                        }
                    }.onFailure { e -> Log.e(tag, "인물 개별 저장 실패: ${person.imageName}", e) }
                }
                successfulPaths.forEach { analyzedImageDao.insertPath(AnalyzedImageEntity(imagePath = it)) }
                Log.d(tag, "${index + 1}번째 청크 완료 및 DB 반영 성공")
            }
            .onFailure { Log.e(tag, "${index + 1}번째 청크 finish 실패: ${it.message}") }
    }

    private suspend fun syncMismatchedNames(dbName: String) {
        personRepository.getMismatchedNames().forEach { (oldName, newName) ->
            runCatching { webService.changePersonName(ChangeNameRequest(dbName, oldName, newName)) }
        }
    }

    private fun isImageFile(path: String): Boolean =
        path.endsWith(".jpg", true) || path.endsWith(".jpeg", true) || path.endsWith(".png", true)

    private fun createMultipartBodyPartFromFilePath(name: String, path: String): MultipartBody.Part {
        val file = File(path)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }
}
