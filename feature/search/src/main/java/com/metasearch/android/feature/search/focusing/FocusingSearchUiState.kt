package com.metasearch.android.feature.search.focusing

import androidx.compose.runtime.Immutable
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.model.CircleModel
import com.metasearch.android.core.model.SearchResult
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

@Immutable
data class FocusingSearchUiState(
    val isLoading: Boolean = false,
    val imageUriString: String,
    val circles: ImmutableList<CircleModel> = persistentListOf(),
    val searchResult: SearchResult? = null,
    val sideEffect: FocusingSearchSideEffect? = null,
    val eventSink: (FocusingSearchUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface FocusingSearchSideEffect {
    data class ShowToast(
        val message: UiText,
        private val key: String = UUID.randomUUID().toString(),
    ) : FocusingSearchSideEffect
}

sealed interface FocusingSearchUiEvent : CircuitUiEvent {
    data object InitSideEffect : FocusingSearchUiEvent

    /**
     * 드래그 완료 시, 원 추가
     */
    data class OnCircleAdded(
        val circle: CircleModel,
    ) : FocusingSearchUiEvent

    /**
     * 이미지 검색 결과 더보기 버튼 클릭
     */
    data class OnMoreClick(
        val categoryName: String,
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
}
