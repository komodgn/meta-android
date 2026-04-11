package com.metasearch.android.data.remote.search.request

import kotlinx.serialization.Serializable

@Serializable
data class DetectedObjectsRequest(
    val dbName: String,
    val properties: List<String>,
)
