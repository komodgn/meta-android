package com.metasearch.android.feature.detail.graph

import com.slack.circuit.runtime.CircuitUiState

data class GraphDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val webViewUrl: String = "",
    val selectedImages: List<String> = emptyList(),
    val eventSink: (GraphDetailUiEvent) -> Unit,
) : CircuitUiState

sealed interface GraphDetailUiEvent {
    data class OnPhotoSelected(
        val photoName: String,
    ) : GraphDetailUiEvent

    /**
     * 헤더의 뒤로 가기 버튼 클릭
     */
    data object OnBackClick : GraphDetailUiEvent

    /**
     * 사진 상세 화면으로 이동하는 이벤트
     */
    data class OnImageClick(
        val uriString: String,
    ) : GraphDetailUiEvent

    data object OnErrorDialogDismiss : GraphDetailUiEvent
}
