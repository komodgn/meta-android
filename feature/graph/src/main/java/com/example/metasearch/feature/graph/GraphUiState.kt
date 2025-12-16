package com.example.metasearch.feature.graph

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class GraphUiState(
    val eventSink: (GraphUiEvent) -> Unit,
) : CircuitUiState

sealed interface GraphUiEvent : CircuitUiEvent {
    data class OnTabClick(
        val screen: Screen,
    ) : GraphUiEvent
}
