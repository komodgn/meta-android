package com.metasearch.android.data.search.impl.repository

import com.metasearch.android.core.common.constants.PromptConstants
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.core.network.request.DetectedObjectsRequest
import com.metasearch.android.core.network.request.FocusingSearchRequest
import com.metasearch.android.core.network.request.NLQueryRequest
import com.metasearch.android.core.network.request.OpenAIMessage
import com.metasearch.android.core.network.request.OpenAIRequest
import com.metasearch.android.core.network.service.AIService
import com.metasearch.android.core.network.service.OpenAIService
import com.metasearch.android.core.network.service.WebService
import com.metasearch.android.data.domain.Circle
import com.metasearch.android.data.domain.DragSearchResult
import com.metasearch.android.data.search.impl.mapper.toModel
import com.metasearch.android.data.search.impl.util.CypherQueryGenerator
import com.metasearch.android.domain.search.api.repository.SearchRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@SingleIn(DataScope::class)
@Inject
class SearchRepositoryImpl(
    private val aiService: AIService,
    private val webService: WebService,
    private val openAIService: OpenAIService,
) : SearchRepository {

    companion object {
        private const val AI_MODEL_NAME = "gpt-3.5-turbo"
    }

    override suspend fun analyzeFocusingImage(
        dbName: String,
        imageFile: File,
        circles: List<Circle>,
    ): List<String> {
        val imagePart = MultipartBody.Part.createFormData(
            "searchImage", imageFile.name, imageFile.asRequestBody("image/jpeg".toMediaType())
        )
        val dbNamePart = dbName.toRequestBody("text/plain".toMediaType())
        val requestCircles = circles.map { com.metasearch.android.core.network.request.Circle(it.centerX, it.centerY, it.radius) }

        val response = aiService.uploadImageAndCircles(
            image = imagePart,
            dbName = dbNamePart,
            request = FocusingSearchRequest(requestCircles)
        )
        return response.detectedObjects
    }

    override suspend fun extractKeywordsFromNL(query: String): List<String> {
        val response = openAIService.createChatCompletion(
            OpenAIRequest(
                model = AI_MODEL_NAME,
                messages = listOf(OpenAIMessage("user", PromptConstants.NL_SEARCH_BASIC_PROMPT + query))
            )
        )
        val content = response.choices.firstOrNull()?.message?.content?.trim() ?: ""
        if (content == "0" || content.isEmpty()) return emptyList()

        return content.split(",").map { it.trim() }.filter { it.isNotBlank() }.distinct().sorted()
    }

    override suspend fun findPhotosByDetectedObjects(
        dbName: String,
        properties: List<String>,
    ): DragSearchResult {
        val response = webService.sendDetectedObjects(
            request = DetectedObjectsRequest(
                dbName = dbName,
                properties = properties,
            ),
        )

        return response?.toModel() ?: DragSearchResult(emptyList())
    }

    override suspend fun searchPhotosByKeywords(dbName: String, keywords: List<String>): List<String> {
        val neo4jQuery = CypherQueryGenerator.generateQueryByKeywords(keywords)
        val response = webService.sendCypherQuery(NLQueryRequest(dbName, neo4jQuery))
        return response?.toModel() ?: emptyList()
    }
}
