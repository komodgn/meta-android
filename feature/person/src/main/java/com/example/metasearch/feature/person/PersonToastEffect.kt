package com.example.metasearch.feature.person

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun PersonToastEffect(
    showToast: Boolean,
    eventSink: (PersonUiEvent) -> Unit,
) {
    LaunchedEffect(showToast) {
        if (showToast) {
            delay(2000L)
            eventSink(PersonUiEvent.HideToast)
        }
    }
}
