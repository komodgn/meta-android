package com.metasearch.android.feature.search.focusing

import com.metasearch.android.core.model.CircleModel
import com.metasearch.android.core.model.SearchResult
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class FocusingSearchUiState(
    val isLoading: Boolean = false,
    val toastMessage: String? = null,
    val imageUriString: String,
    val circles: List<CircleModel>? = emptyList(),
    val searchResult: SearchResult? = null,
    val eventSink: (FocusingSearchUiEvent) -> Unit,
) : CircuitUiState

sealed interface FocusingSearchUiEvent : CircuitUiEvent {
    /**
     * 드래그 완료 시, 원 추가
     */
    data class OnCircleAdded(
        val circle: CircleModel,
    ) : FocusingSearchUiEvent

    /**
     * 포커싱 검색 이벤트
     */
    data object OnSearchClick : FocusingSearchUiEvent

    /**
     * 원 색상 변경 이벤트
     */
    data object OnColorClick : FocusingSearchUiEvent

    /**
     * 이미지 위에 그려진 원 리스트 초기화
     */
    data object OnCircleResetClick : FocusingSearchUiEvent

    /**
     * 헤더 뒤로가기 버튼 클릭
     */
    data object OnBackClick : FocusingSearchUiEvent

    /**
     * 검색된 사진 클릭 시, 해당 사진 상세 화면으로 이동
     */
    data class OnImageClick(
        val imageUriString: String,
    ) : FocusingSearchUiEvent

    data class ShowToast(
        val message: String? = null,
    ) : FocusingSearchUiEvent

    data object HideToast : FocusingSearchUiEvent
}
