package com.example.metasearch.feature.search.focusing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun FocusingSearchToastEffect(
    isCirclesEmpty: Boolean,
    eventSink: (FocusingSearchUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        if (isCirclesEmpty) {
            eventSink(FocusingSearchUiEvent.ShowToast)
            delay(1500L)
            eventSink(FocusingSearchUiEvent.HideToast)
        }
    }
}
