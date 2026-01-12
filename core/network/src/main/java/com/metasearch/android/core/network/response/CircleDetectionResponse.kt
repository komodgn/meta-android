package com.metasearch.android.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CircleDetectionResponse(
    val message: String,
    @SerialName("detected_objects")
    val detectedObjects: List<String>,
)
