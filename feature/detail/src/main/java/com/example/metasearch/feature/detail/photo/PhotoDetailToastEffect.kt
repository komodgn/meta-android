package com.example.metasearch.feature.detail.photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun PhotoDetailToastEffect(
    toastMessage: String? = null,
    eventSink: (PhotoDetailUiEvent) -> Unit,
) {
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(1500L)

            eventSink(PhotoDetailUiEvent.HideToast)
        }
    }
}
