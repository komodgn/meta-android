package com.metasearch.android.feature.webview

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class WebViewUiState(
    val isLoading: Boolean = false,
    val url: String = "",
    val eventSink: (WebViewUiEvent) -> Unit,
) : CircuitUiState

sealed interface WebViewUiEvent : CircuitUiEvent
