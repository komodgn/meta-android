package com.example.metasearch.feature.splash

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class SplashUiState(
    val permissions: List<String> = emptyList(),
    val showRationaleDialog: Boolean = false,
    val navigateToSettings: Boolean = false,
    val eventSink: (SplashUiEvent) -> Unit,
) : CircuitUiState

sealed interface SplashUiEvent : CircuitUiEvent {
    data class PermissionResult(
        val allGranted: Boolean
    ) : SplashUiEvent
    data object OnConfirmSettings : SplashUiEvent
    data object OnResetSettingsNavigation : SplashUiEvent
}
