package com.metasearch.android.domain.graph.api.repository

interface GraphRepository {
    suspend fun getFullGraphWebViewUrl(): String
    suspend fun getDetailGraphWebViewUrl(entityName: String): String
}
