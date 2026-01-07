package com.example.metasearch.feature.home

import androidx.paging.PagingData
import com.example.metasearch.core.model.GalleryImageModel
import com.example.metasearch.core.model.PersonModel
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
    val eventSink: (HomeUiEvent) -> Unit,
) : CircuitUiState

sealed interface HomeUiEvent : CircuitUiEvent {
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

    /**
     * 하단 네비 탭 클릭
     */
    data class OnTabClick(
        val screen: Screen,
    ) : HomeUiEvent
}
