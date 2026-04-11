package com.metasearch.android.data.remote.search.request

import kotlinx.serialization.Serializable

@Serializable
data class NLQueryRequest(
    val dbName: String,
    val query: String,
)
