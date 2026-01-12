package com.metasearch.android.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class DetectedObjectsRequest(
    val dbName: String,
    val properties: List<String>,
)
