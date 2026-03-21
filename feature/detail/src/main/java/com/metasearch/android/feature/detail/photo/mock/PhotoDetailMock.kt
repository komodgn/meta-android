package com.metasearch.android.feature.detail.photo.mock

import com.metasearch.android.feature.detail.photo.PhotoDetailUiState

fun PhotoDetailUiState.Companion.mock() = PhotoDetailUiState(
    imageUriString = "uri",
    eventSink = {},
)
