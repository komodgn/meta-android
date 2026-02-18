package com.metasearch.android.feature.search.nls

import androidx.compose.runtime.Immutable
import com.metasearch.android.core.common.utils.UiText
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import java.util.UUID

data class NLSearchUiState(
    val isLoading: Boolean = false,
    val inputString: String = "",
    val resultImages: List<String> = emptyList(),
    val sideEffect: NLSearchSideEffect? = null,
    val eventSink: (NLSearchUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface NLSearchSideEffect {
    data class ShowToast(
        val message: UiText,
        private val key: String = UUID.randomUUID().toString(),
    ) : NLSearchSideEffect
}

sealed interface NLSearchUiEvent : CircuitUiEvent {
    data object InitSideEffect : NLSearchUiEvent

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
