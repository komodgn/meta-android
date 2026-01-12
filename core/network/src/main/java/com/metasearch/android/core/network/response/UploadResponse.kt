package com.metasearch.android.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val images: List<PersonAnalysisResult>,
)

@Serializable
data class PersonAnalysisResult(
    val imageName: String? = null,
    val imageBytes: String? = null,
    val isFaceExit: Boolean,
)
