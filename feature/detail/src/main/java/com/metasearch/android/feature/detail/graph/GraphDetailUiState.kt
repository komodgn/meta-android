package com.metasearch.android.feature.detail.graph

import androidx.compose.runtime.Immutable
import com.metasearch.android.core.common.utils.UiText
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

@Immutable
sealed interface UiState {
    data object Loading : UiState
    data object Success : UiState
    data class Error(val message: String) : UiState
}

data class GraphDetailUiState(
    val uiState: UiState = UiState.Loading,
    val webViewUrl: String = "",
    val selectedImages: ImmutableList<String> = persistentListOf(),
    val sideEffect: GraphDetailSideEffect? = null,
    val eventSink: (GraphDetailUiEvent) -> Unit,
) : CircuitUiState {
    companion object
}

@Immutable
sealed interface GraphDetailSideEffect {
    data class ShowToast(
        val message: UiText,
        private val key: String = UUID.randomUUID().toString(),
    ) : GraphDetailSideEffect
}

sealed interface GraphDetailUiEvent {
    data object InitSideEffect : GraphDetailUiEvent

    data object OnWebLoading : GraphDetailUiEvent
    data object OnWebSuccess : GraphDetailUiEvent
    data class OnWebError(
        val message: String,
    ) : GraphDetailUiEvent
    data object OnRetry : GraphDetailUiEvent

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
