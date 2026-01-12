package com.metasearch.android.core.model

import androidx.compose.runtime.Stable

@Stable
data class GalleryImageModel(
    val id: Long, // MediaStore._ID
    val uriString: String,
    val dateAdded: Long,
)
