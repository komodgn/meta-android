package com.metasearch.android.feature.search.focusing.mock

import com.metasearch.android.core.model.PhotoGroup
import com.metasearch.android.core.model.SearchResult
import com.metasearch.android.feature.search.focusing.FocusingSearchUiState
import kotlinx.collections.immutable.persistentListOf

internal val fakePhotoGroups = listOf(
    PhotoGroup(
        categoryName = "# 햄버거 # 감자튀김",
        photoNames = listOf("https://picsum.photos/200", "https://picsum.photos/201", "https://picsum.photos/202", "https://picsum.photos/203"),
    ),
    PhotoGroup(
        categoryName = "# 햄버거",
        photoNames = listOf("https://picsum.photos/204", "https://picsum.photos/205"),
    ),
    PhotoGroup(
        categoryName = "# 감자튀김",
        photoNames = listOf("https://picsum.photos/206", "https://picsum.photos/207"),
    ),
)

internal val fakeSearchResult = SearchResult(groups = fakePhotoGroups)

internal val focusingSearchUiStateMock = FocusingSearchUiState(
    imageUriString = "https://picsum.photos/400/600",
    searchResult = fakeSearchResult,
    isLoading = false,
    circles = persistentListOf(),
    eventSink = {},
)
