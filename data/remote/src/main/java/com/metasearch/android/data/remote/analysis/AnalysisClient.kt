package com.metasearch.android.data.remote.analysis

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.remote.analysis.request.DeleteImageRequest
import com.metasearch.android.data.remote.analysis.request.OpenAIMessage
import com.metasearch.android.data.remote.analysis.request.OpenAIRequest
import com.metasearch.android.data.remote.analysis.response.TripleResponse
import com.metasearch.android.data.remote.analysis.response.UploadResponse
import com.metasearch.android.data.remote.analysis.service.AnalysisAIService
import com.metasearch.android.data.remote.analysis.service.AnalysisOpenAIService
import com.metasearch.android.data.remote.analysis.service.AnalysisWebService
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@SingleIn(DataScope::class)
@Inject
class AnalysisClient(
    private val analysisAIService: AnalysisAIService,
    private val analysisWebService: AnalysisWebService,
    private val analysisOpenAIService: AnalysisOpenAIService,
) {

    companion object {
        private const val AI_MODEL_NAME = "gpt-3.5-turbo"
    }

    suspend fun uploadImage(
        dbName: String,
        fileName: String,
        imageFile: File,
    ) = coroutineScope {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val webImagePart = MultipartBody.Part.createFormData("image", fileName, requestFile)
        val aiImagePart = MultipartBody.Part.createFormData("addImage", fileName, requestFile)
        val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

        val webJob = async { analysisWebService.uploadWebAddImage(webImagePart, dbName) }
        val aiJob = async { analysisAIService.uploadAddImage(aiImagePart, dbNameBody) }

        awaitAll(webJob, aiJob)
    }

    suspend fun deleteImage(fileName: String, dbName: String) = coroutineScope {
        val webJob = async { analysisWebService.uploadWebDeleteImage(DeleteImageRequest(dbName, fileName)) }
        val aiPart = MultipartBody.Part.createFormData("deleteImage", fileName, "".toRequestBody())
        val aiJob = async { analysisAIService.uploadDeleteImage(aiPart, dbName.toRequestBody()) }

        awaitAll(webJob, aiJob)
    }

    suspend fun finishAnalysis(dbName: String, lastIndex: Int): UploadResponse {
        val finishBody = "true".toRequestBody("text/plain".toMediaTypeOrNull())
        val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())
        val countBody = lastIndex.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        return analysisAIService.uploadFinish(finishBody, dbNameBody, countBody)
    }

    suspend fun fetchTripleData(dbName: String, photoName: String): TripleResponse {
        return analysisWebService.fetchTripleData(dbName, photoName)
    }

    suspend fun getChatCompletion(prompt: String): String {
        val request = OpenAIRequest(
            model = AI_MODEL_NAME,
            messages = listOf(OpenAIMessage(role = "user", content = prompt)),
        )
        val response = analysisOpenAIService.createChatCompletion(request)
        return response.choices.firstOrNull()?.message?.content ?: error("AI response is empty")
    }
}
