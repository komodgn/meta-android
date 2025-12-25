package com.example.metasearch.feature.detail.person

import android.net.Uri
import com.example.metasearch.core.model.PersonModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class PersonDetailUiState(
    val isLoading: Boolean = false,
    val person: PersonModel? = null,
    val photoUris: List<Uri> = emptyList(),
    val eventSink: (PersonDetailUiEvent) -> Unit,
) : CircuitUiState

sealed interface PersonDetailUiEvent : CircuitUiEvent {
    /**
     * 헤더의 뒤로 가기 버튼 클릭
     */
    data object OnHeaderBackClick : PersonDetailUiEvent

    /**
     * 그리드 영역 이미지 클릭
     */
    data class OnGridImageClick(
        val imageUri: Uri,
    ) : PersonDetailUiEvent
}
