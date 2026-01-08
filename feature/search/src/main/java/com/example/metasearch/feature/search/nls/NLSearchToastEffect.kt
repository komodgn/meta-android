package com.example.metasearch.feature.search.nls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun NLSearchToastEffect(
    toastMessage: String? = null,
    eventSink: (NLSearchUiEvent) -> Unit,
) {
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2000L)
            eventSink(NLSearchUiEvent.HideToast)
        }
    }
}
