package com.metasearch.android.feature.detail.graph.mock

import com.metasearch.android.feature.detail.graph.GraphDetailUiState
import kotlinx.collections.immutable.toPersistentList

internal val fakeSelectedImages = (1..10).map { i ->
    "https://picsum.photos/seed/$i/200/200"
}.toPersistentList()

fun GraphDetailUiState.Companion.mock() = GraphDetailUiState(
    webViewUrl = "https://www.google.com",
    selectedImages = fakeSelectedImages,
    eventSink = {},
)
