package com.metasearch.android.core.data.impl.repository

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Base64.decode
import android.util.Log
import androidx.core.net.toUri
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.metasearch.android.core.common.constants.PromptConstants
import com.metasearch.android.core.common.extensions.toFile
import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.data.api.repository.DatabaseNameRepository
import com.metasearch.android.core.data.api.repository.GalleryRepository
import com.metasearch.android.core.data.api.repository.ImageAnalysisRepository
import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.core.data.api.repository.SearchRepository
import com.metasearch.android.core.datastore.api.datasource.PersonIndexDataSource
import com.metasearch.android.core.network.request.ChangeNameRequest
import com.metasearch.android.core.network.request.DeleteImageRequest
import com.metasearch.android.core.network.request.OpenAIMessage
import com.metasearch.android.core.network.request.OpenAIRequest
import com.metasearch.android.core.network.service.AIService
import com.metasearch.android.core.network.service.OpenAIService
import com.metasearch.android.core.network.service.WebService
import com.metasearch.android.core.room.api.dao.AnalyzedImageDao
import com.metasearch.android.core.room.api.entity.AnalyzedImageEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope
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
    private val searchRepository: SearchRepository,
    private val aiService: AIService,
    private val webService: WebService,
    private val openAIService: OpenAIService,
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
        val deleteCount = deleteMissingImages(alreadyAnalyzedPaths, currentGalleryUrisString, dbName)
        if (deleteCount > 0) {
            searchRepository.clearEntityCache()
        }
        Log.d(tag, "6. 삭제 로직 완료")

        val addUris = currentGalleryUris.filter { uri ->
            uri.toString() !in alreadyAnalyzedPaths
        }
        Log.d(tag, "7. 추가할 이미지 수: ${addUris.size}")

        if (addUris.isNotEmpty()) {
            val allSuccessfulData = mutableListOf<Pair<String, String>>()

            addUris.chunked(chunkSize).forEach { chunk ->
                val successfulInChunk = uploadOnlyImageChunk(context, chunk, dbName)
                allSuccessfulData.addAll(successfulInChunk)
            }

            if (allSuccessfulData.isNotEmpty()) {
                processAnalysisFinish(allSuccessfulData, dbName)

                searchRepository.clearEntityCache()
            }
        } else {
            Log.d(tag, "8. 추가할 이미지가 없어 종료함")
        }

        syncMismatchedNames(dbName)
    }

    override suspend fun getImageDescription(uriString: String): Result<String> = runSuspendCatching {
        coroutineScope {
            val uri = uriString.toUri()
            val photoNameDeferred = async { galleryRepository.getFileName(uri) }
            val dbNameDeferred = async { databaseNameRepository.getPersistentDeviceDatabaseName() }

            val photoName = photoNameDeferred.await()
                ?: error("파일 이름을 찾을 수 없음.")
            val dbName = dbNameDeferred.await()

            val tripleDataResponse = webService.fetchTripleData(dbName, photoName)
            val fullPrompt = PromptConstants.CREATE_IMAGE_BASIC_PROMPT + tripleDataResponse.triple
            val imageDescriptionResponse = openAIService.createChatCompletion(
                OpenAIRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(OpenAIMessage(role = "user", content = fullPrompt)),
                ),
            )

            imageDescriptionResponse.choices.firstOrNull()?.message?.content
                ?: error("AI 응답 내용이 비어 있음.")
        }
    }

    private suspend fun deleteMissingImages(
        alreadyPaths: List<String>,
        currentPaths: List<String>,
        dbName: String,
    ): Int {
        val deletePaths = alreadyPaths.filter { it !in currentPaths }
        Log.d(tag, "삭제 대상 개수: ${deletePaths.size}개")
        deletePaths.forEachIndexed { index, pathString ->
            Log.d(tag, "이미지 삭제 중 (${index + 1}/${deletePaths.size}): $pathString")

            val savedFileName = analyzedImageDao.getFileNameByPath(pathString)
            Log.d(tag, "서버로 보낼 파일명: $savedFileName")

            val webResult = runCatching {
                webService.uploadWebDeleteImage(DeleteImageRequest(dbName, savedFileName ?: "unknown.jpg"))
            }

            val aiPart = MultipartBody.Part.createFormData("deleteImage", savedFileName, "".toRequestBody())
            val aiResult = runCatching {
                aiService.uploadDeleteImage(aiPart, dbName.toRequestBody())
            }

            if (webResult.isSuccess && aiResult.isSuccess) {
                analyzedImageDao.deletePath(pathString)
                Log.d(tag, "삭제 성공: $pathString")
            } else {
                Log.e(tag, "삭제 실패 (Web: $webResult, AI: $aiResult): $pathString")
            }
        }

        return deletePaths.size
    }

    private suspend fun uploadOnlyImageChunk(
        context: Context,
        chunk: List<Uri>,
        dbName: String,
    ): List<Pair<String, String>> = supervisorScope {
        chunk.map { uri ->
            async {
                val fileName = galleryRepository.getFileName(uri) ?: "unknown.jpg"
                if (uploadSingleImage(context, uri, dbName, fileName)) uri.toString() to fileName else null
            }
        }.awaitAll().filterNotNull()
    }

    private suspend fun uploadSingleImage(context: Context, uri: Uri, dbName: String, fileName: String): Boolean {
        val tempFile = uri.toFile(context)

        return try {
            val webImagePart = createMultipartBodyPartFromUri(context, "image", uri, fileName)
            val aiImagePart = createMultipartBodyPartFromUri(context, "addImage", uri, fileName)
            val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

            webService.uploadWebAddImage(webImagePart, dbName)
            aiService.uploadAddImage(aiImagePart, dbNameBody)

            Log.d(tag, "서버 전송 성공 (파일명: $fileName): $uri")
            true
        } catch (e: Exception) {
            Log.e(tag, "서버 전송 실패 원인: ${e.message}", e)
            false
        } finally {
            tempFile.delete()
        }
    }

    private suspend fun processAnalysisFinish(successfulPaths: List<Pair<String, String>>, dbName: String) {
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

                analyzedImageDao.runInTransaction {
                    if (newMax > lastIndex) {
                        personIndexDataSource.setLastPersonIndex(newMax)
                    }

                    response.images.forEach { person ->
                        runCatching {
                            if (person.isFaceExit && person.imageName != null && person.imageBytes != null) {
                                val decodedBytes = decode(person.imageBytes, Base64.DEFAULT)

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

                    val entries = successfulPaths.map { (path, fileName) ->
                        AnalyzedImageEntity(imagePath = path, fileName = fileName)
                    }
                    analyzedImageDao.insertAllPaths(entries)
                }

                Log.d(tag, "트랜잭션 완료: 인물 및 경로(${successfulPaths.size})개 저장 성공")
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
