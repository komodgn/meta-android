package com.metasearch.android.feature.search.nls

import androidx.compose.runtime.Immutable
import com.metasearch.android.core.common.utils.UiText
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

data class NLSearchUiState(
    val isLoading: Boolean = false,
    val inputString: String = "",
    val resultImages: ImmutableList<String> = persistentListOf(),
    val isLocalEngineReady: Boolean = false,
    val isLocalSearchEnabled: Boolean = false,
    val sideEffect: NLSearchSideEffect? = null,
    val eventSink: (NLSearchUiEvent) -> Unit,
) : CircuitUiState {
    companion object
}

@Immutable
sealed interface NLSearchSideEffect {
    data class ShowToast(
        val message: UiText,
        private val key: String = UUID.randomUUID().toString(),
    ) : NLSearchSideEffect
}

sealed interface NLSearchUiEvent : CircuitUiEvent {
    data object InitSideEffect : NLSearchUiEvent

    data class OnInputChange(
        val inputString: String,
    ) : NLSearchUiEvent

    data class OnNLSearchClick(
        val inputString: String,
    ) : NLSearchUiEvent

    data class OnImageClick(
        val imageUriString: String,
    ) : NLSearchUiEvent

    data class OnTabClick(
        val screen: Screen,
    ) : NLSearchUiEvent

    data class OnToggleLocalSearch(
        val isEnabled: Boolean,
    ) : NLSearchUiEvent
}
