package com.metasearch.android.feature.detail.graph

import androidx.compose.runtime.Immutable
import com.metasearch.android.core.common.utils.UiText
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class GraphDetailUiState(
    val isLoading: Boolean = false,
    val webViewUrl: String = "",
    val selectedImages: List<String> = emptyList(),
    val sideEffect: GraphDetailSideEffect? = null,
    val eventSink: (GraphDetailUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface GraphDetailSideEffect {
    data class ShowToast(
        val message: UiText,
        private val key: String = UUID.randomUUID().toString(),
    ) : GraphDetailSideEffect
}

sealed interface GraphDetailUiEvent {
    data object InitSideEffect : GraphDetailUiEvent

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
}
