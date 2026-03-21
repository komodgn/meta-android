package com.metasearch.android.core.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

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

fun PhotoGroup.Companion.fakes(): PersistentList<PhotoGroup> = listOf(
    PhotoGroup(
        categoryName = "햄버거, 감자튀김",
        photoNames = listOf(
            "https://picsum.photos/200",
            "https://picsum.photos/201",
            "https://picsum.photos/202",
            "https://picsum.photos/203",
        ).toPersistentList(),
    ),
    PhotoGroup(
        categoryName = "햄버거",
        photoNames = listOf(
            "https://picsum.photos/204",
            "https://picsum.photos/205",
        ).toPersistentList(),
    ),
    PhotoGroup(
        categoryName = "감자튀김",
        photoNames = listOf(
            "https://picsum.photos/206",
            "https://picsum.photos/207",
        ).toPersistentList(),
    ),
).toPersistentList()
