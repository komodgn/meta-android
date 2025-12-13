package com.example.metasearch.core.model

import androidx.compose.runtime.Stable

@Stable
data class HomeModel(
    val galleryImages: List<GalleryImageModel> = emptyList(),

    val homeDisplayPeople: List<PersonModel> = emptyList(),
)

@Stable
data class GalleryImageModel(
    val uriString: String,
    val width: Int = 0,
    val height: Int = 0,
)
