package com.example.metasearch.feature.home

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class HomeUiState(
    val isLoading: Boolean,
    val eventSink: (HomeUiEvent) -> Unit,
): CircuitUiState

sealed interface HomeUiEvent: CircuitUiEvent {

}
