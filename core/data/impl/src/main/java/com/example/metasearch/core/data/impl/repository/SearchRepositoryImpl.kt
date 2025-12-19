package com.example.metasearch.core.data.impl.repository

import android.content.Context
import com.example.metasearch.core.common.constants.PromptConstants
import com.example.metasearch.core.data.api.repository.DatabaseNameRepository
import com.example.metasearch.core.data.api.repository.SearchRepository
import com.example.metasearch.core.data.impl.mapper.toModel
import com.example.metasearch.core.data.impl.util.CypherQueryGenerator
import com.example.metasearch.core.model.CircleModel
import com.example.metasearch.core.model.NLSearchResult
import com.example.metasearch.core.model.SearchResult
import com.example.metasearch.core.network.request.DetectedObjectsRequest
import com.example.metasearch.core.network.request.FocusingSearchRequest
import com.example.metasearch.core.network.request.NLQueryRequest
import com.example.metasearch.core.network.request.OpenAIMessage
import com.example.metasearch.core.network.request.OpenAIRequest
import com.example.metasearch.core.network.service.AIService
import com.example.metasearch.core.network.service.OpenAIService
import com.example.metasearch.core.network.service.WebService
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map
import com.example.metasearch.core.network.request.Circle as RequestCircle

@Singleton
internal class SearchRepositoryImpl @Inject constructor(
    private val aiService: AIService,
    private val webService: WebService,
    private val openAIService: OpenAIService,
    private val databaseNameRepository: DatabaseNameRepository,
    @ApplicationContext private val context: Context,
) : SearchRepository {
    override suspend fun focusingSearch(
        imageFile: File,
        circles: List<CircleModel>,
    ): Result<SearchResult> = runCatching {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        val imagePart = MultipartBody.Part.createFormData(
            "searchImage",
            imageFile.name,
            imageFile.asRequestBody("image/jpeg".toMediaType()),
        )

        val dbNamePart = dbName.toRequestBody("text/plain".toMediaType())

        val requestCircles = circles.map {
            RequestCircle(it.centerX, it.centerY, it.radius)
        }
        val detectionResponse = aiService.uploadImageAndCircles(
            image = imagePart,
            dbName = dbNamePart,
            request = FocusingSearchRequest(requestCircles),
        )

        val finalResult = webService.sendDetectedObjects(
            request = DetectedObjectsRequest(
                dbName = dbName,
                properties = detectionResponse.detectedObjects,
            ),
        )

        finalResult?.toModel() ?: SearchResult(emptyList())
    }

    override suspend fun nlSearch(
        query: String,
    ): Result<NLSearchResult> = runCatching {
        if (query.isBlank()) return@runCatching NLSearchResult(emptyList())

        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        val fullPrompt = PromptConstants.NL_SEARCH_BASIC_PROMPT + query
        val openAIResponse = openAIService.createChatCompletion(
            request = OpenAIRequest(
                model = "gpt-3.5-turbo",
                messages = listOf(OpenAIMessage(role = "user", content = fullPrompt)),
            ),
        )

        val text = openAIResponse.choices.firstOrNull()?.message?.content?.trim() ?: ""
        if (text == "0" || text.isEmpty()) return@runCatching NLSearchResult(emptyList())

        val entities = text.split(",").map { it.trim() }
        val neo4jQuery = CypherQueryGenerator.generateQueryByKeywords(
            keywords = entities,
        )

        val response = webService.sendCypherQuery(
            request = NLQueryRequest(
                dbName = dbName,
                query = neo4jQuery,
            ),
        )

        response.toModel(this.context)
    }
}
