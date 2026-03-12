package com.metasearch.android.feature.detail.photo

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import com.metasearch.android.core.common.utils.UiText
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class PhotoDetailUiState(
    val isLoading: Boolean = false,
    val imageUriString: String,
    val imageDescription: String? = null,
    val sideEffect: PhotoDetailSideEffect? = null,
    val eventSink: (PhotoDetailUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface PhotoDetailSideEffect {
    data class ShowToast(
        val message: UiText,
        private val key: String = UUID.randomUUID().toString(),
    ) : PhotoDetailSideEffect

    data class ShareImage(
        val uriString: String,
    ) : PhotoDetailSideEffect
}

sealed interface PhotoDetailUiEvent : CircuitUiEvent {
    data object InitSideEffect : PhotoDetailUiEvent

    /**
     * Open AI로 이미지 설명을 생성
     */
    data class OnCreateImageDescriptionClick(
        val imageUriString: String,
    ) : PhotoDetailUiEvent

    /**
     *  개별 그래프 확인 버튼 클릭
     */
    data object OnGraphClick : PhotoDetailUiEvent

    /**
     * 포커싱 검색 화면으로 이동
     */
    data class OnFocusingSearchClick(
        val imageUriString: String,
    ) : PhotoDetailUiEvent

    /**
     * 이미지 공유 버튼 클릭
     */
    data class OnShareImageClick(
        val imageUriString: String,
    ) : PhotoDetailUiEvent

    /**
     * 헤더의 뒤로가기 버튼 클릭
     */
    data object OnBackClick : PhotoDetailUiEvent
}
