package com.example.metasearch.feature.search.nls

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class NLSearchUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val inputString: String = "",
    val resultImages: List<String> = emptyList(),
    val eventSink: (NLSearchUiEvent) -> Unit,
) : CircuitUiState

sealed interface NLSearchUiEvent : CircuitUiEvent {
    /**
     * 입력 텍스트 동기화
     */
    data class OnInputChange(
        val inputString: String,
    ) : NLSearchUiEvent

    /**
     * 자연어 검색 버튼 클릭
     */
    data class OnNLSearchClick(
        val inputString: String,
    ) : NLSearchUiEvent

    data object OnDialogCloseButtonClick : NLSearchUiEvent

    /**
     * 개별 이미지 클릭
     */
    data class OnImageClick(
        val imageUriString: String,
    ) : NLSearchUiEvent

    /**
     * 하단 네비 탭 클릭
     */
    data class OnTabClick(
        val screen: Screen,
    ) : NLSearchUiEvent
}
