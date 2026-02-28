package com.metasearch.android.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class SearchResult(
    val groups: List<PhotoGroup>,
)

@Immutable
data class PhotoGroup(
    val categoryName: String,
    val photoNames: List<String>,
)

@Immutable
data class NLSearchResult(
    val matchedUris: List<String>,
)
