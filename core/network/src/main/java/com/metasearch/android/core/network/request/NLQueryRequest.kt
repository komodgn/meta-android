package com.metasearch.android.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class NLQueryRequest(
    val dbName: String,
    val query: String,
)
