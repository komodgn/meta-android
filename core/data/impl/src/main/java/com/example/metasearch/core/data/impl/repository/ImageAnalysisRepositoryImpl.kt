package com.example.metasearch.core.data.impl.repository

import android.content.Context
import android.net.Uri
import android.util.Base64.decode
import android.util.Log
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.metasearch.core.common.utils.toFile
import com.example.metasearch.core.data.api.repository.DatabaseNameRepository
import com.example.metasearch.core.data.api.repository.GalleryRepository
import com.example.metasearch.core.data.api.repository.ImageAnalysisRepository
import com.example.metasearch.core.data.api.repository.PersonRepository
import com.example.metasearch.core.datastore.api.datasource.PersonIndexDataSource
import com.example.metasearch.core.network.request.ChangeNameRequest
import com.example.metasearch.core.network.service.AIService
import com.example.metasearch.core.network.service.WebService
import com.example.metasearch.core.room.api.dao.AnalyzedImageDao
import com.example.metasearch.core.room.api.entity.AnalyzedImageEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@Suppress("LongParameterList")
class ImageAnalysisRepositoryImpl @Inject constructor(
    private val analyzedImageDao: AnalyzedImageDao,
    private val galleryRepository: GalleryRepository,
    private val databaseNameRepository: DatabaseNameRepository,
    private val personIndexDataSource: PersonIndexDataSource,
    private val personRepository: PersonRepository,
    private val aiService: AIService,
    private val webService: WebService,
    @ApplicationContext private val context: Context,
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
        Log.d(tag, "1. runFullAnalysis 함수 실행")

        val currentGalleryUris = galleryRepository.getAllGalleryImages()
        Log.d(tag, "2. 갤러리 이미지 로드 완료: ${currentGalleryUris.size}개")
        val currentGalleryUrisString = currentGalleryUris.map { it.toString() }

        val alreadyAnalyzedPaths = analyzedImageDao.getAllAnalyzedPaths()
        Log.d(tag, "3. 기존 분석 경로 로드 완료: ${alreadyAnalyzedPaths.size}개")
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        Log.d(tag, "5. 삭제 로직 시작")
        deleteMissingImages(alreadyAnalyzedPaths, currentGalleryUrisString, dbName)
        Log.d(tag, "6. 삭제 로직 완료")

        val addUris = currentGalleryUris.filter { uri ->
            uri.toString() !in alreadyAnalyzedPaths
        }
        Log.d(tag, "7. 추가할 이미지 수: ${addUris.size}")

        if (addUris.isNotEmpty()) {
            val allSuccessfulPaths = mutableListOf<String>()

            addUris.chunked(chunkSize).forEachIndexed { index, chunk ->
                val successfulInChunk = uploadOnlyImageChunk(context, index, chunk, dbName)
                allSuccessfulPaths.addAll(successfulInChunk)

                if (successfulInChunk.size != chunk.size) return@forEachIndexed
            }

            if (allSuccessfulPaths.isNotEmpty()) {
                processAnalysisFinish(allSuccessfulPaths, dbName)
            }
        } else {
            Log.d(tag, "8. 추가할 이미지가 없어 종료함")
        }

        syncMismatchedNames(dbName)
    }

    private suspend fun deleteMissingImages(alreadyPaths: List<String>, currentPaths: List<String>, dbName: String) {
        val deletePaths = alreadyPaths.filter { it !in currentPaths }
        Log.d(tag, "삭제 대상 개수: ${deletePaths.size}개")
        deletePaths.forEachIndexed { index, pathString ->
            Log.d(tag, "이미지 삭제 중 (${index + 1}/${deletePaths.size}): $pathString")
            val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

            val fileName = pathString.substringAfterLast('/')
            val dummyRequestBody = "".toRequestBody("image/*".toMediaTypeOrNull())

            val fileNamePart = MultipartBody.Part.createFormData("deleteImage", fileName, dummyRequestBody)

            val webSuccess = runCatching { webService.uploadWebDeleteImage(fileNamePart, dbName) }.isSuccess
            val aiSuccess = runCatching { aiService.uploadDeleteImage(fileNamePart, dbNameBody) }.isSuccess

            if (webSuccess && aiSuccess) {
                analyzedImageDao.deletePath(pathString)
                Log.d(tag, "삭제 성공: $pathString")
            } else {
                Log.e(tag, "삭제 실패 (Web: $webSuccess, AI: $aiSuccess): $pathString")
            }
        }
    }

    private suspend fun uploadOnlyImageChunk(context: Context, index: Int, chunk: List<Uri>, dbName: String): List<String> {
        Log.d(tag, "청크 전송 중: ${index + 1}번째 (${chunk.size}개)")
        val successfulPaths = mutableListOf<String>()

        for (uri in chunk) {
            if (uploadSingleImage(context, uri, dbName)) {
                successfulPaths.add(uri.toString())
            } else {
                Log.e(tag, "업로드 실패: $uri")
                break
            }
        }
        return successfulPaths
    }

    private suspend fun uploadSingleImage(context: Context, uri: Uri, dbName: String): Boolean {
        val tempFile = uri.toFile(context)

        return try {
            val originalFileName = galleryRepository.getFileName(uri) ?: "unknown.jpg"

            val webImagePart = createMultipartBodyPartFromUri(context, "image", uri, originalFileName)
            val aiImagePart = createMultipartBodyPartFromUri(context, "addImage", uri, originalFileName)
            val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

            webService.uploadWebAddImage(webImagePart, dbName)
            aiService.uploadAddImage(aiImagePart, dbNameBody)

            Log.d(tag, "서버 전송 성공 (파일명: $originalFileName): $uri")
            true
        } catch (e: Exception) {
            Log.e(tag, "서버 전송 실패 원인: ${e.message}", e)
            false
        } finally {
            tempFile.delete()
        }
    }

    private suspend fun processAnalysisFinish(successfulPaths: List<String>, dbName: String) {
        val lastIndex = personIndexDataSource.getLastPersonIndex()

        Log.d(tag, lastIndex.toString())
        val finishBody = "true".toRequestBody("text/plain".toMediaTypeOrNull())
        val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())
        val countBody = lastIndex.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        runCatching { aiService.uploadFinish(finishBody, dbNameBody, countBody) }
            .onSuccess { response ->
                Log.d(tag, "서버로부터 수신한 인물 수: ${response.images.size}")

                val newMax = response.images
                    .mapNotNull { it.imageName?.filter { c -> c.isDigit() }?.toIntOrNull() }
                    .maxOrNull() ?: lastIndex

                if (newMax > lastIndex) {
                    personIndexDataSource.setLastPersonIndex(newMax)
                }

                response.images.forEach { person ->
                    runCatching {
                        if (person.isFaceExit && person.imageName != null && person.imageBytes != null) {
                            val decodedBytes = decode(person.imageBytes, android.util.Base64.DEFAULT)

                            val existingPersonId = personRepository.getPersonIdByImageName(person.imageName!!)

                            if (existingPersonId != null) {
                                Log.d(tag, "기존 인물 매핑 성공: ${person.imageName} -> ID:$existingPersonId")
                                personRepository.addFaceToExistingPerson(existingPersonId, person.imageName!!, decodedBytes)
                            } else {
                                Log.d(tag, "새로운 인물 생성: ${person.imageName}")
                                personRepository.addAnalyzedPerson(person.imageName!!, decodedBytes)
                            }
                        } else {
                            Log.w(tag, "저장 스킵됨: 얼굴없음(${!person.isFaceExit}) 또는 데이터가 null임")
                        }
                    }.onFailure { e -> Log.e(tag, "인물 개별 저장 실패: ${person.imageName}", e) }
                }
                successfulPaths.forEach { analyzedImageDao.insertPath(AnalyzedImageEntity(imagePath = it)) }
                Log.d(tag, "전체 분석 및 DB 반영 완료")
            }
            .onFailure { Log.e(tag, "최종 finish 실패: ${it.message}") }
    }

    private suspend fun syncMismatchedNames(dbName: String) {
        val mismatches = personRepository.getMismatchedFaceNames()

        mismatches.forEach { (serverName, actualName) ->
            runCatching {
                webService.changePersonName(ChangeNameRequest(dbName, serverName, actualName))
            }
        }
    }

    private fun createMultipartBodyPartFromUri(context: Context, name: String, uri: Uri, fileName: String): MultipartBody.Part {
        val file = uri.toFile(context)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(name, fileName, requestFile)
    }
}
