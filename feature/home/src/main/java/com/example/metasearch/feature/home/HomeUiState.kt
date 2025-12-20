package com.example.metasearch.feature.home

import android.net.Uri
import com.example.metasearch.core.model.PersonModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class HomeUiState(
    val isLoading: Boolean = false,
    val isExpanded: Boolean = false,
    val persons: List<PersonModel> = emptyList(),
    val images: List<Uri> = emptyList(),
    val eventSink: (HomeUiEvent) -> Unit,
) : CircuitUiState

sealed interface HomeUiEvent : CircuitUiEvent {
    /**
     *
     */
    data object OnPersonSectionExpand : HomeUiEvent
    /**
     * 상단 인물 클릭
     */
    data class OnPersonClick(
        val personId: Int,
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
