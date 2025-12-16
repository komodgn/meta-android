package com.example.metasearch.feature.home

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class HomeUiState(
    val isLoading: Boolean = false,
    val eventSink: (HomeUiEvent) -> Unit,
) : CircuitUiState

sealed interface HomeUiEvent : CircuitUiEvent {
    data class OnTabClick(
        val screen: Screen,
    ) : HomeUiEvent
}
