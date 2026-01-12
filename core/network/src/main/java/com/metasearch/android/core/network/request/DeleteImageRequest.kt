package com.metasearch.android.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteImageRequest(
    val dbName: String,
    val deleteImage: String,
)
