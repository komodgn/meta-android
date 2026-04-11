package com.metasearch.android.data.remote.search.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CircleDetectionResponse(
    val message: String,
    @SerialName("detected_objects")
    val detectedObjects: List<String>,
)
