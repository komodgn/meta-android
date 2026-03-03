package com.metasearch.android.feature.search.nls.mock

import com.metasearch.android.feature.search.nls.NLSearchUiState
import kotlinx.collections.immutable.toImmutableList

internal val fakeResultImages = listOf(
    "uri1",
    "uri2",
)

internal val nlSearchUiStateMock = NLSearchUiState(
    isLoading = false,
    resultImages = fakeResultImages.toImmutableList(),
    eventSink = {},
)
