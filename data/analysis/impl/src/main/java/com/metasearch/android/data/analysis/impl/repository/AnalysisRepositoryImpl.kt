package com.metasearch.android.data.analysis.impl.repository

import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.core.network.request.DeleteImageRequest
import com.metasearch.android.core.network.request.OpenAIMessage
import com.metasearch.android.core.network.request.OpenAIRequest
import com.metasearch.android.core.network.service.AIService
import com.metasearch.android.core.network.service.OpenAIService
import com.metasearch.android.core.network.service.WebService
import com.metasearch.android.core.room.api.dao.AnalyzedImageDao
import com.metasearch.android.core.room.api.entity.AnalyzedImageEntity
import com.metasearch.android.data.analysis.impl.mapper.toModel
import com.metasearch.android.data.domain.AnalysisResult
import com.metasearch.android.data.domain.UploadedImage
import com.metasearch.android.domain.analysis.api.repository.AnalysisRepository
import com.metasearch.android.domain.device.api.repository.DatabaseNameRepository
import com.metasearch.android.domain.file.api.repository.FileRepository
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.supervisorScope
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Suppress("LongParameterList")
@SingleIn(DataScope::class)
@Inject
class AnalysisRepositoryImpl(
    private val aiService: AIService,
    private val webService: WebService,
    private val openAIService: OpenAIService,
    private val databaseNameRepository: DatabaseNameRepository,
    private val galleryRepository: GalleryRepository,
    private val fileRepository: FileRepository,
    private val analyzedImageDao: AnalyzedImageDao,
) : AnalysisRepository {

    companion object {
        private const val AI_MODEL_NAME = "gpt-3.5-turbo"
    }

    override suspend fun getAlreadyAnalyzedPaths(): List<String> =
        analyzedImageDao.getAllAnalyzedPaths()

    override suspend fun getFileNameByPath(path: String): String? =
        analyzedImageDao.getFileNameByPath(path)

    override suspend fun saveAnalyzedPath(path: String, fileName: String): Result<Unit> = runSuspendCatching {
        analyzedImageDao.insertPath(AnalyzedImageEntity(imagePath = path, fileName = fileName))
    }

    override suspend fun uploadImages(
        uriStrings: List<String>,
        dbName: String,
    ): List<UploadedImage> = supervisorScope {
        uriStrings.map { uriString ->
            async {
                runSuspendCatching {
                    val fileName = galleryRepository.getFileName(uriString) ?: "unknown.jpg"
                    val tempFile = fileRepository.createTempFileFromUri(uriString).getOrThrow()

                    try {
                        val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                        val webImagePart = MultipartBody.Part.createFormData("image", fileName, requestFile)
                        val aiImagePart = MultipartBody.Part.createFormData("addImage", fileName, requestFile)
                        val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

                        val webJob = async { webService.uploadWebAddImage(webImagePart, dbName) }
                        val aiJob = async { aiService.uploadAddImage(aiImagePart, dbNameBody) }

                        awaitAll(webJob, aiJob)

                        UploadedImage(uriString = uriString, fileName = fileName)
                    } finally {
                        fileRepository.deleteFile(tempFile)
                    }
                }.getOrNull()
            }
        }.awaitAll().filterNotNull()
    }

    override suspend fun deleteLocalPath(path: String): Result<Unit> = runSuspendCatching {
        analyzedImageDao.deletePath(path)
    }

    override suspend fun deleteImage(fileName: String, dbName: String): Result<Unit> = runSuspendCatching {
        coroutineScope {
            val webJob = async { webService.uploadWebDeleteImage(DeleteImageRequest(dbName, fileName)) }
            val aiPart = MultipartBody.Part.createFormData("deleteImage", fileName, "".toRequestBody())
            val aiJob = async { aiService.uploadDeleteImage(aiPart, dbName.toRequestBody()) }

            awaitAll(webJob, aiJob)
        }
    }

    override suspend fun finishAnalysis(
        dbName: String,
        lastIndex: Int,
    ): Result<AnalysisResult> = runSuspendCatching {
        val finishBody = "true".toRequestBody("text/plain".toMediaTypeOrNull())
        val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())
        val countBody = lastIndex.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val response = aiService.uploadFinish(finishBody, dbNameBody, countBody)

        response.toModel()
    }

    override suspend fun getImageTripleData(photoName: String): Result<String> = runSuspendCatching {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
        val response = webService.fetchTripleData(dbName, photoName)

        response.triple
    }

    override suspend fun getAiCompletion(prompt: String): Result<String> = runSuspendCatching {
        val request = OpenAIRequest(
            model = AI_MODEL_NAME,
            messages = listOf(OpenAIMessage(role = "user", content = prompt)),
        )
        val response = openAIService.createChatCompletion(request)
        response.choices.firstOrNull()?.message?.content ?: error("AI response is empty")
    }
}
