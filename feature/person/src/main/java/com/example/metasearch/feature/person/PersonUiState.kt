package com.example.metasearch.feature.person

import com.example.metasearch.core.model.PersonModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class PersonUiState(
    val isLoading: Boolean = false,
    val inputPersonNameString: String = "",
    val people: List<PersonModel> = emptyList(),
    val eventSink: (PersonUiEvent) -> Unit,
) : CircuitUiState

sealed interface PersonUiEvent : CircuitUiEvent {
    /**
     * 입력 텍스트 동기화
     */
    data class OnInputChange(
        val inputString: String,
    ) : PersonUiEvent

    /**
     * 인물 검색 버튼 클릭
     */
    data class OnPersonSearchClick(
        val inputString: String,
    ) : PersonUiEvent

    /**
     * 클릭한 인물의 모든 사진을 볼 수 있는 상세 화면으로 이동
     */
    data class OnPersonClick(
        val personId: Long,
    ) : PersonUiEvent

    /**
     * 하단 네비 탭 클릭
     */
    data class OnTabClick(
        val screen: Screen,
    ) : PersonUiEvent
}
