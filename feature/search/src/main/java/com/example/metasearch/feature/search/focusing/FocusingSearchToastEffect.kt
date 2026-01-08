package com.example.metasearch.feature.search.focusing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun FocusingSearchToastEffect(
    toastMessage: String? = null,
    eventSink: (FocusingSearchUiEvent) -> Unit,
) {
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(1500L)
            eventSink(FocusingSearchUiEvent.HideToast)
        }
    }
}
