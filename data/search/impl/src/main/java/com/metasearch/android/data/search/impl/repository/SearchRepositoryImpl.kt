package com.metasearch.android.data.search.impl.repository

import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.domain.Circle
import com.metasearch.android.data.domain.DragSearchResult
import com.metasearch.android.data.remote.search.SearchClient
import com.metasearch.android.data.remote.search.util.CypherQueryGenerator
import com.metasearch.android.data.search.impl.mapper.toModel
import com.metasearch.android.domain.search.api.repository.SearchRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.io.File

@SingleIn(DataScope::class)
@Inject
class SearchRepositoryImpl(
    private val searchClient: SearchClient,
) : SearchRepository {

    override suspend fun analyzeFocusingImage(
        dbName: String,
        imageFile: File,
        circles: List<Circle>,
    ): List<String> = runSuspendCatching {
        val response = searchClient.analyzeFocusingImage(dbName, imageFile, circles)
        response.detectedObjects
    }.getOrDefault(emptyList())

    override suspend fun extractKeywordsFromNL(query: String): List<String> = runSuspendCatching {
        searchClient.extractKeywords(query)
    }.getOrDefault(emptyList())

    override suspend fun findPhotosByDetectedObjects(
        dbName: String,
        properties: List<String>,
    ): DragSearchResult = runSuspendCatching {
        val response = searchClient.fetchPhotosByObjects(dbName, properties)
        response.toModel()
    }.getOrDefault(DragSearchResult(emptyList()))

    override suspend fun searchPhotosByKeywords(dbName: String, keywords: List<String>): List<String> = runSuspendCatching {
        val neo4jquery = CypherQueryGenerator.generateQueryByKeywords(keywords)
        val response = searchClient.fetchPhotosByKeywords(dbName, neo4jquery)
        response.toModel()
    }.getOrDefault(emptyList())
}
