package com.metasearch.android.feature.person

import androidx.compose.runtime.Immutable
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.model.Person
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

data class PersonUiState(
    val isLoading: Boolean = false,
    val inputPersonNameString: String = "",
    val showDeleteDialog: Boolean = false,
    val pendingDeletePersonName: String = "",
    val people: ImmutableList<Person> = persistentListOf(),
    val sideEffect: PersonSideEffect? = null,
    val eventSink: (PersonUiEvent) -> Unit,
) : CircuitUiState {
    companion object
}

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

    data class OnPersonDeleteClick(
        val personId: Long,
    ) : PersonUiEvent
    data object OnPersonDeleteConfirm : PersonUiEvent
    data object OnPersonDeleteCancel : PersonUiEvent

    /**
     * Navigate to PersonDetail Screen
     */
    data class OnPersonClick(
        val personId: Long,
    ) : PersonUiEvent

    data class OnTabClick(
        val screen: Screen,
    ) : PersonUiEvent
}
