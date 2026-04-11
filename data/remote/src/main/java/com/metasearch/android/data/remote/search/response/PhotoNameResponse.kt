package com.metasearch.android.data.remote.search.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoNameResponse(
    @SerialName("PhotoName")
    val photoNames: List<String>,
)
