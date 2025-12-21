package com.example.metasearch.feature.detail.person

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class PersonDetailUiState(
    val isLoading: Boolean = false,
    val eventSink: (PersonDetailUiEvent) -> Unit,
) : CircuitUiState

sealed interface PersonDetailUiEvent : CircuitUiEvent {
    /**
     * 헤더의 뒤로 가기 버튼 클릭
     */
    data object OnHeaderBackClick : PersonDetailUiEvent
}
