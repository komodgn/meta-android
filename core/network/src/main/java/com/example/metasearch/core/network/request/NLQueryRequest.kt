package com.example.metasearch.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class NLQueryRequest(
    val dbName: String,
    val query: String,
)
