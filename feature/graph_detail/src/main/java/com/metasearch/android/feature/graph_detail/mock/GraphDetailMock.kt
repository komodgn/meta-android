package com.metasearch.android.feature.graph_detail.mock

import com.metasearch.android.feature.graph_detail.GraphDetailUiState
import kotlinx.collections.immutable.toPersistentList

internal val fakeSelectedImages = (1..10).map { i ->
    "https://picsum.photos/seed/$i/200/200"
}.toPersistentList()

fun GraphDetailUiState.Companion.mock() = GraphDetailUiState(
    webViewUrl = "https://www.google.com",
    selectedImages = fakeSelectedImages,
    eventSink = {},
)
