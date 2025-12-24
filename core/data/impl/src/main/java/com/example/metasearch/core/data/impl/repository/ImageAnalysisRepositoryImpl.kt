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

        val deletePaths = alreadyAnalyzedPaths.filter { it !in currentGalleryPaths }
        deletePaths.forEach { path ->
            val fileNamePart = createMultipartBodyPartFromFilePath("deleteImage", path)
            val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

            val webSuccess = runCatching { webService.uploadWebDeleteImage(fileNamePart, dbName) }.isSuccess
            val aiSuccess = runCatching { aiService.uploadDeleteImage(fileNamePart, dbNameBody) }.isSuccess

            if (webSuccess && aiSuccess) { analyzedImageDao.deletePath(path) }
        }

        val addPaths = currentGalleryPaths.filter { path ->
            path !in alreadyAnalyzedPaths &&
                (path.endsWith(".jpg", true) || path.endsWith(".jpeg", true) || path.endsWith(".png", true))
        }
        if (addPaths.isEmpty()) return@withContext

        val chunkSize = 10
        addPaths.chunked(chunkSize).forEachIndexed { index, chunk ->
            Log.d(tag, "청크 처리 중: ${index + 1}번째 묶음 (${chunk.size}개)")

            val successfulPathsInChunk = mutableListOf<String>()
            var isChunkAllUploadSuccess = true

            for (path in chunk) {
                val webImagePart = createMultipartBodyPartFromFilePath("image", path)
                val aiImagePart = createMultipartBodyPartFromFilePath("addImage", path)
                val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

                val webSuccess = runCatching { webService.uploadWebAddImage(webImagePart, dbName) }.isSuccess
                val aiSuccess = runCatching { aiService.uploadAddImage(aiImagePart, dbNameBody) }

                if (aiSuccess.isSuccess) {
                    successfulPathsInChunk.add(path)
                } else {
                    isChunkAllUploadSuccess = false
                    Log.e(tag, "업로드 실패로 인한 청크 중단: $path")
                    break
                }
            }

            if (isChunkAllUploadSuccess) {
                val currentPersonCount = personRepository.getPersonCount()
                val finishBody = "true".toRequestBody("text/plain".toMediaTypeOrNull())
                val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())
                val countBody = currentPersonCount.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                val finishResult = runCatching {
                    aiService.uploadFinish(finishBody, dbNameBody, countBody)
                }

                val finishResponse = finishResult.getOrNull()

                if (finishResult.isFailure) {
                    Log.e(tag, "uploadFinish 실제 에러 원인: ${finishResult.exceptionOrNull()?.message}")
                }

                if (finishResponse != null) {
                    finishResponse.images.forEach { personResult ->
                        Log.d(tag, "데이터 수신됨: ${personResult.imageName}")
                        if (personResult.isFaceExit && personResult.imageName != null && personResult.imageBytes != null) {
                            val decodedBytes = decode(personResult.imageBytes, android.util.Base64.DEFAULT)
                            personRepository.addAnalyzedPerson(personResult.imageName!!, decodedBytes)
                            Log.d(tag, "addAnalyzedPerson 호출 완료")
                        }
                    }

                    successfulPathsInChunk.forEach { path ->
                        analyzedImageDao.insertPath(AnalyzedImageEntity(imagePath = path))
                    }
                    Log.d(tag, "${index + 1}번째 청크 완료 및 로컬 DB 반영 성공")
                } else {
                    Log.e(tag, "${index + 1}번째 청크 uploadFinish 실패. 다음 분석에서 재시도")
                }
            }
        }

        val mismatchedNames = personRepository.getMismatchedNames()
        mismatchedNames.forEach { (oldName, newName) ->
            webService.changeName(ChangeNameRequest(dbName, oldName, newName))
        }
    }

    private fun createMultipartBodyPartFromFilePath(name: String, path: String): MultipartBody.Part {
        val file = File(path)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }
}
