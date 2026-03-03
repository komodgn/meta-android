package com.metasearch.android.feature.detail.graph.mock

import com.metasearch.android.feature.detail.graph.GraphDetailUiState
import kotlinx.collections.immutable.toPersistentList

internal val fakeSelectedImages = listOf(
    "sample_uri_1",
    "sample_uri_2",
)

internal val graphDetailUiStateMock = GraphDetailUiState(
    webViewUrl = "https://www.google.com",
    selectedImages = fakeSelectedImages.toPersistentList(),
    eventSink = {},
)
