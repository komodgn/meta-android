package com.metasearch.android.data.remote.analysis.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteImageRequest(
    val dbName: String,
    val deleteImage: String,
)
