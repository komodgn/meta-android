package com.example.metasearch.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
    val name: String,
    val entityType: String,
    val imageUrl: String,
)
