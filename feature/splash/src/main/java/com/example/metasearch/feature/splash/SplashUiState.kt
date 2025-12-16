package com.example.metasearch.feature.splash

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class SplashUiState(
    val eventSink: (SplashUiEvent) -> Unit,
): CircuitUiState

sealed interface SplashUiEvent: CircuitUiEvent {
    data object OnNavigationToNextScreen: SplashUiEvent
}
