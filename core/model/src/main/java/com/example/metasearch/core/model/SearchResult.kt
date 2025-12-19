package com.example.metasearch.core.model

import androidx.compose.runtime.Stable

@Stable
data class SearchResult(
    val groups: List<PhotoGroup>,
)

@Stable
data class PhotoGroup(
    val categoryName: String,
    val photoNames: List<String>,
)
