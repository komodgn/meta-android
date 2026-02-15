package com.metasearch.android.feature.person

import androidx.compose.runtime.Immutable
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.model.PersonModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import java.util.UUID

data class PersonUiState(
    val isLoading: Boolean = false,
    val inputPersonNameString: String = "",
    val showDeleteDialog: Boolean = false,
    val pendingDeletePersonName: String = "",
    val people: List<PersonModel> = emptyList(),
    val sideEffect: PersonSideEffect? = null,
    val eventSink: (PersonUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface PersonSideEffect {
    data class ShowToast(
        val message: UiText,
        private val key: String = UUID.randomUUID().toString(),
    ) : PersonSideEffect
}

sealed interface PersonUiEvent : CircuitUiEvent {
    data object InitSideEffect : PersonUiEvent

    /**
     * 입력 텍스트 동기화
     */
    data class OnInputChange(
        val inputString: String,
    ) : PersonUiEvent

    /**
     * 인물 삭제 버튼 클릭
     */
    data class OnPersonDeleteClick(
        val personId: Long,
    ) : PersonUiEvent

    /**
     * 삭제 확인 다이얼로그의 삭제 버튼 클릭 이벤트
     */
    data object OnPersonDeleteConfirm : PersonUiEvent

    data object OnPersonDeleteCancel : PersonUiEvent

    /**
     * 클릭한 인물의 모든 사진을 볼 수 있는 상세 화면으로 이동
     */
    data class OnPersonClick(
        val personId: Long,
    ) : PersonUiEvent

    /**
     * 하단 네비 탭 클릭
     */
    data class OnTabClick(
        val screen: Screen,
    ) : PersonUiEvent
}
