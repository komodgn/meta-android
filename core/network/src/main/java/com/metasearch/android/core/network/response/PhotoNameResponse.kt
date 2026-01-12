package com.metasearch.android.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoNameResponse(
    @SerialName("PhotoName")
    val photoNames: List<String>,
)
