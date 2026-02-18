package com.metasearch.android.feature.graph

import androidx.compose.runtime.Immutable
import com.metasearch.android.core.common.utils.UiText
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import java.util.UUID

data class GraphUiState(
    val webViewUrl: String = "",
    val selectedImages: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val sideEffect: GraphSideEffect? = null,
    val eventSink: (GraphUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface GraphSideEffect {
    data class ShowToast(
        val message: UiText,
        private val key: String = UUID.randomUUID().toString(),
    ) : GraphSideEffect
}

sealed interface GraphUiEvent : CircuitUiEvent {
    data object InitSideEffect : GraphUiEvent

    data class OnPhotoSelected(
        val photoName: String,
    ) : GraphUiEvent

    /**
     * 사진 상세 화면으로 이동
     */
    data class OnImageClick(
        val uriString: String,
    ) : GraphUiEvent

    /**
     * 하단 네비 탭 클릭
     */
    data class OnTabClick(
        val screen: Screen,
    ) : GraphUiEvent
}
