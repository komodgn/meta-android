package com.example.metasearch.feature.search.nls

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class NLSearchUiState(
    val eventSink: (NLSearchUiEvent) -> Unit,
) : CircuitUiState

sealed interface NLSearchUiEvent : CircuitUiEvent {
    data class OnTabClick(
        val screen: Screen,
    ) : NLSearchUiEvent
}
