package com.metasearch.android.data.domain

data class NLSearchResult(
    val matchedUris: List<String>,
) {
    companion object
}

fun NLSearchResult.Companion.fake() = NLSearchResult(
    matchedUris = listOf("uri1", "uri2"),
)
