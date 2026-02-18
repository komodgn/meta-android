package com.metasearch.android.feature.home

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.paging.PagingData
import com.metasearch.android.core.model.GalleryImageModel
import com.metasearch.android.core.model.PersonModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.flow.Flow

data class HomeUiState(
    val isPersonLoading: Boolean = false,
    val isAnalyzing: Boolean = false,
    val isExpanded: Boolean = false,
    val persons: List<PersonModel> = emptyList(),
    val images: Flow<PagingData<GalleryImageModel>>,
    val selectedLongClickImage: String? = null,
    val selectedOffset: Offset = Offset.Zero,
    val sideEffect: HomeSideEffect? = null,
    val eventSink: (HomeUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface HomeSideEffect {
    data class ShareImage(
        val uriString: String,
    ) : HomeSideEffect
}

sealed interface HomeUiEvent : CircuitUiEvent {
    data object InitSideEffect : HomeUiEvent

    /**
     * 이미지 분석 요청 버튼 클릭
     */
    data object OnStartAnalysisClicked : HomeUiEvent

    /**
     * 상단 인물 리스트 영역 클릭
     */
    data object OnPersonSectionExpand : HomeUiEvent

    /**
     * 상단 인물 클릭
     */
    data class OnPersonClick(
        val personId: Long,
    ) : HomeUiEvent

    /**
     * 개별 이미지 클릭
     */
    data class OnImageClick(
        val imageUriString: String,
    ) : HomeUiEvent

    data class OnImageLongClick(
        val imageUriString: String,
        val offSet: Offset = Offset.Zero,
    ) : HomeUiEvent

    data object OnLongClickCancel : HomeUiEvent

    data class OnShareRelease(
        val imageUriString: String,
    ) : HomeUiEvent

    /**
     * 하단 네비 탭 클릭
     */
    data class OnTabClick(
        val screen: Screen,
    ) : HomeUiEvent
}
