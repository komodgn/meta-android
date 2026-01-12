package com.metasearch.android.feature.detail.photo

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class PhotoDetailUiState(
    val isLoading: Boolean = false,
    val toastMessage: String? = null,
    val imageUriString: String,
    val imageDescription: String? = null,
    val eventSink: (PhotoDetailUiEvent) -> Unit,
) : CircuitUiState

sealed interface PhotoDetailUiEvent : CircuitUiEvent {
    /**
     * Open AI로 이미지 설명을 생성
     */
    data class OnCreateImageDescriptionButtonClick(
        val imageUriString: String,
    ) : PhotoDetailUiEvent

    /**
     *  개별 그래프 확인 버튼 클릭
     */
    data object OnGraphButtonClick : PhotoDetailUiEvent

    /**
     * 포커싱 검색 화면으로 이동
     */
    data class OnFocusingSearchClick(
        val imageUriString: String,
    ) : PhotoDetailUiEvent

    /**
     * 이미지 공유 버튼 클릭
     */
    data class OnShareImageButtonClick(
        val imageUriString: String,
    ) : PhotoDetailUiEvent

    /**
     * 헤더의 뒤로가기 버튼 클릭
     */
    data object OnBackClick : PhotoDetailUiEvent

    data class ShowToast(
        val message: String,
    ) : PhotoDetailUiEvent

    data object HideToast : PhotoDetailUiEvent
}
