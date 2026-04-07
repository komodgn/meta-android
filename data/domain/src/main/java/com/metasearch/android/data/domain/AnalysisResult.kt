package com.metasearch.android.data.domain

data class AnalysisResult(
    val detectedPersons: List<DetectedPerson>,
)

data class DetectedPerson(
    val imageName: String?,
    val imageBytes: ByteArray?,
    val isFaceExist: Boolean,
)
