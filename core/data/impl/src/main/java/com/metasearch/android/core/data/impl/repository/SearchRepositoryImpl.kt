package com.metasearch.android.core.data.impl.repository

import com.metasearch.android.core.common.constants.PromptConstants
import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.data.api.repository.DatabaseNameRepository
import com.metasearch.android.core.data.api.repository.GalleryRepository
import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.core.data.api.repository.SearchRepository
import com.metasearch.android.core.data.impl.mapper.toModel
import com.metasearch.android.core.data.impl.util.CypherQueryGenerator
import com.metasearch.android.core.model.CircleModel
import com.metasearch.android.core.model.NLSearchResult
import com.metasearch.android.core.model.SearchResult
import com.metasearch.android.core.network.request.DetectedObjectsRequest
import com.metasearch.android.core.network.request.FocusingSearchRequest
import com.metasearch.android.core.network.request.NLQueryRequest
import com.metasearch.android.core.network.request.OpenAIMessage
import com.metasearch.android.core.network.request.OpenAIRequest
import com.metasearch.android.core.network.service.AIService
import com.metasearch.android.core.network.service.OpenAIService
import com.metasearch.android.core.network.service.WebService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import com.metasearch.android.core.network.request.Circle as RequestCircle

@Singleton
internal class SearchRepositoryImpl @Inject constructor(
    private val aiService: AIService,
    private val webService: WebService,
    private val openAIService: OpenAIService,
    private val galleryRepository: GalleryRepository,
    private val personRepository: PersonRepository,
    private val databaseNameRepository: DatabaseNameRepository,
) : SearchRepository {
    private val entityCache = androidx.collection.LruCache<String, List<String>>(30)

    override suspend fun focusingSearch(
        imageFile: File,
        circles: List<CircleModel>,
    ): Result<SearchResult> = runSuspendCatching {
        coroutineScope {
            val dbNameDeferred = async { databaseNameRepository.getPersistentDeviceDatabaseName() }
            val imagePartDeferred = async {
                MultipartBody.Part.createFormData(
                    "searchImage",
                    imageFile.name,
                    imageFile.asRequestBody("image/jpeg".toMediaType()),
                )
            }

            val dbName = dbNameDeferred.await()
            val imagePart = imagePartDeferred.await()
            val dbNamePart = dbName.toRequestBody("text/plain".toMediaType())
            val requestCircles = circles.map {
                RequestCircle(it.centerX, it.centerY, it.radius)
            }

            val detectionResponse = aiService.uploadImageAndCircles(
                image = imagePart,
                dbName = dbNamePart,
                request = FocusingSearchRequest(requestCircles),
            )

            val mappedProperties = detectionResponse.detectedObjects.map { systemName ->
                val inputName = personRepository.getInputNameBySystemName(systemName)

                if (!inputName.isNullOrBlank()) inputName else systemName
            }.filter { it.isNotBlank() }.distinct()

            if (mappedProperties.isEmpty()) return@coroutineScope SearchResult(groups = emptyList())

            val finalResult = webService.sendDetectedObjects(
                request = DetectedObjectsRequest(
                    dbName = dbName,
                    properties = mappedProperties,
                ),
            )

            val searchResult = finalResult?.toModel() ?: SearchResult(emptyList())
            val updatedGroups = searchResult.groups.map { group ->
                async {
                    val matchedUris = galleryRepository.findMatchedUris(group.photoNames)
                    group.copy(photoNames = matchedUris.map { it.toString() })
                }
            }.awaitAll().filter { it.photoNames.isNotEmpty() }

            SearchResult(groups = updatedGroups)
        }
    }

    override suspend fun nlSearch(
        query: String,
    ): Result<NLSearchResult> = runSuspendCatching {
        if (query.isBlank()) return@runSuspendCatching NLSearchResult(emptyList())

        coroutineScope {
            val dbNameDeferred = async { databaseNameRepository.getPersistentDeviceDatabaseName() }
            val openAIResponseDeferred = async {
                val fullPrompt = PromptConstants.NL_SEARCH_BASIC_PROMPT + query
                openAIService.createChatCompletion(
                    request = OpenAIRequest(
                        model = "gpt-3.5-turbo",
                        messages = listOf(OpenAIMessage(role = "user", content = fullPrompt)),
                    ),
                )
            }

            val openAIResponse = openAIResponseDeferred.await()
            val text = openAIResponse.choices.firstOrNull()?.message?.content?.trim() ?: ""

            if (text == "0" || text.isEmpty()) return@coroutineScope NLSearchResult(emptyList())

            val entities = text.split(",").map { it.trim() }.sorted()
            val entityKey = entities.joinToString(",")

            entityCache.get(entityKey)?.let { cachedUris ->
                val result = NLSearchResult(cachedUris)
                return@coroutineScope result
            }

            val neo4jQuery = CypherQueryGenerator.generateQueryByKeywords(keywords = entities)
            val dbName = dbNameDeferred.await()
            val response = webService.sendCypherQuery(
                request = NLQueryRequest(
                    dbName = dbName,
                    query = neo4jQuery,
                ),
            )

            val photoNames = response.toModel()

            val matchedUris = galleryRepository.findMatchedUris(photoNames).map { it.toString() }

            val finalResult = NLSearchResult(matchedUris = matchedUris)

            entityCache.put(entityKey, matchedUris)

            finalResult
        }
    }

    override fun clearEntityCache() {
        entityCache.evictAll()
    }
}
