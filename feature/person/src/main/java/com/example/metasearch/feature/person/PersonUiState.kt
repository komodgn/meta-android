package com.example.metasearch.feature.person

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class PersonUiState(
    val eventSink: (PersonUiEvent) -> Unit,
) : CircuitUiState

sealed interface PersonUiEvent : CircuitUiEvent {
    data class OnTabClick(
        val screen: Screen
    ) : PersonUiEvent
}
