package com.metasearch.android.feature.graph

import androidx.compose.runtime.Immutable
import com.metasearch.android.core.common.utils.UiText
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

@Immutable
sealed interface UiState {
    data object Loading : UiState
    data object Success : UiState
    data class Error(val message: String) : UiState
}

data class GraphUiState(
    val uiState: UiState = UiState.Loading,
    val webViewUrl: String = "",
    val selectedImages: ImmutableList<String> = persistentListOf(),
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

    data object OnWebLoading : GraphUiEvent
    data object OnWebSuccess : GraphUiEvent
    data class OnWebError(
        val message: String,
    ) : GraphUiEvent
    data object OnRetry : GraphUiEvent

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
