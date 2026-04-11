package com.metasearch.android.data.remote.search

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.remote.analysis.request.OpenAIMessage
import com.metasearch.android.data.remote.analysis.request.OpenAIRequest
import com.metasearch.android.data.remote.search.constant.PromptConstants
import com.metasearch.android.data.remote.search.request.Circle
import com.metasearch.android.data.remote.search.request.DetectedObjectsRequest
import com.metasearch.android.data.remote.search.request.FocusingSearchRequest
import com.metasearch.android.data.remote.search.request.NLQueryRequest
import com.metasearch.android.data.remote.search.response.CircleDetectionResponse
import com.metasearch.android.data.remote.search.response.PhotoNameResponse
import com.metasearch.android.data.remote.search.response.PhotoResponse
import com.metasearch.android.data.remote.search.service.SearchAIService
import com.metasearch.android.data.remote.search.service.SearchOpenAIService
import com.metasearch.android.data.remote.search.service.SearchWebService
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@SingleIn(DataScope::class)
@Inject
class SearchClient(
    private val searchAIService: SearchAIService,
    private val searchWebService: SearchWebService,
    private val searchOpenAIService: SearchOpenAIService,
) {
    companion object {
        private const val AI_MODEL_NAME = "gpt-3.5-turbo"
    }

    suspend fun analyzeFocusingImage(
        dbName: String,
        imageFile: File,
        circles: List<com.metasearch.android.data.domain.Circle>,
    ): CircleDetectionResponse {
        val requestCircles = circles.map { Circle(it.centerX, it.centerY, it.radius) }

        val imagePart = MultipartBody.Part.createFormData(
            "searchImage",
            imageFile.name,
            imageFile.asRequestBody("image/jpeg".toMediaType()),
        )
        val dbNamePart = dbName.toRequestBody("text/plain".toMediaType())

        return searchAIService.uploadImageAndCircles(
            image = imagePart,
            dbName = dbNamePart,
            request = FocusingSearchRequest(requestCircles),
        )
    }

    suspend fun extractKeywords(query: String): List<String> {
        val response = searchOpenAIService.createChatCompletion(
            OpenAIRequest(
                model = AI_MODEL_NAME,
                messages = listOf(OpenAIMessage("user", PromptConstants.NL_SEARCH_BASIC_PROMPT + query)),
            ),
        )
        val content = response.choices.firstOrNull()?.message?.content?.trim() ?: ""

        if (content == "0" || content.isEmpty()) return emptyList()

        return content.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    suspend fun fetchPhotosByObjects(
        dbName: String,
        properties: List<String>,
    ): PhotoResponse {
        return searchWebService.sendDetectedObjects(
            DetectedObjectsRequest(dbName, properties),
        )
    }

    suspend fun fetchPhotosByKeywords(
        dbName: String,
        neo4jQuery: String,
    ): PhotoNameResponse {
        return searchWebService.sendCypherQuery(
            NLQueryRequest(dbName, neo4jQuery),
        )
    }
}
