package com.example.metasearch.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoNameResponse(
    @SerialName("PhotoName")
    val photoNames: List<String>,
)
