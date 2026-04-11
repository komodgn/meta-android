package com.metasearch.android.data.remote.search.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
    @SerialName("photos")
    val photos: Photos,
)

@Serializable
data class Photos(
    @SerialName("commonPhotos")
    val commonPhotos: List<String>,
    @SerialName("individualPhotos")
    val individualPhotos: Map<String, List<String>>,
)
