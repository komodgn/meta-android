package com.metasearch.android.feature.search.focusing.mock

import com.metasearch.android.data.domain.DragSearchResult
import com.metasearch.android.data.domain.fakes
import com.metasearch.android.feature.search.focusing.FocusingSearchUiState
import kotlinx.collections.immutable.persistentListOf

fun FocusingSearchUiState.Companion.mock(): FocusingSearchUiState = FocusingSearchUiState(
    imageUriString = "https://picsum.photos/400/600",
    searchResult = DragSearchResult.fakes(),
    isLoading = false,
    circles = persistentListOf(),
    eventSink = {},
)
