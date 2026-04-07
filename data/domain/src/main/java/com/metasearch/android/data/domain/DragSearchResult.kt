package com.metasearch.android.data.domain

import androidx.compose.runtime.Immutable

@Immutable
data class PhotoGroup(
    val categoryName: String,
    val photoNames: List<String>,
) {
    companion object
}

@Immutable
data class DragSearchResult(
    val groups: List<PhotoGroup>,
) {
    companion object
}

fun DragSearchResult.Companion.fakes(): DragSearchResult = DragSearchResult(
    groups = PhotoGroup.fakes(),
)

fun PhotoGroup.Companion.fakes() = listOf(
    PhotoGroup(
        categoryName = "햄버거, 감자튀김",
        photoNames = listOf(
            "https://picsum.photos/200",
            "https://picsum.photos/201",
            "https://picsum.photos/202",
            "https://picsum.photos/203",
        ),
    ),
    PhotoGroup(
        categoryName = "햄버거",
        photoNames = listOf(
            "https://picsum.photos/204",
            "https://picsum.photos/205",
        ),
    ),
    PhotoGroup(
        categoryName = "감자튀김",
        photoNames = listOf(
            "https://picsum.photos/206",
            "https://picsum.photos/207",
        ),
    ),
)
