package com.metasearch.android.feature.splash

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SplashUiState(
    val permissions: ImmutableList<String> = persistentListOf(),
    val navigateToSettings: Boolean = false,
    val eventSink: (SplashUiEvent) -> Unit,
) : CircuitUiState {
    companion object
}

fun SplashUiState.Companion.mock() = SplashUiState(
    eventSink = {},
)

sealed interface SplashUiEvent : CircuitUiEvent {
    data class PermissionResult(
        val allGranted: Boolean,
    ) : SplashUiEvent
    data object OnConfirmSettings : SplashUiEvent
    data object OnResetSettingsNavigation : SplashUiEvent
}
