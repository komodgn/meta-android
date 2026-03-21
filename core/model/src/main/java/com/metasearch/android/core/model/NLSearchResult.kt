package com.metasearch.android.core.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class NLSearchResult(
    val matchedUris: List<String>,
) {
    companion object
}

fun NLSearchResult.Companion.fake(): NLSearchResult = NLSearchResult(
    matchedUris = listOf(
        "https://picsum.photos/300",
        "https://picsum.photos/301",
    ).toPersistentList(),
)
