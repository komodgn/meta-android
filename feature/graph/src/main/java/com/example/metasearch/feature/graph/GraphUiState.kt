package com.example.metasearch.feature.graph

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class GraphUiState(
    val webViewUrl: String = "",
    val selectedImages: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val eventSink: (GraphUiEvent) -> Unit,
) : CircuitUiState

sealed interface GraphUiEvent : CircuitUiEvent {
    data class OnPhotoSelected(
        val photoName: String,
    ) : GraphUiEvent

    /**
     * 사진 상세 화면으로 이동
     */
    data class OnImageClick(
        val uriString: String,
    ) : GraphUiEvent

    data object OnErrorDialogDismiss : GraphUiEvent

    /**
     * 하단 네비 탭 클릭
     */
    data class OnTabClick(
        val screen: Screen,
    ) : GraphUiEvent
}
