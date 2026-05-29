package com.metasearch.android.feature.photo_detail.mock

import com.metasearch.android.feature.photo_detail.PhotoDetailUiState

fun PhotoDetailUiState.Companion.mock() = PhotoDetailUiState(
    imageUriString = "uri",
    eventSink = {},
)