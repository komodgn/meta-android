package com.metasearch.android.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.metasearch.android.core.common.extensions.openSettings
import com.metasearch.android.core.permissions.api.ui.PermissionsState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashSideEffectHandler(
    state: SplashUiState,
    permissionState: PermissionsState,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasReturnedFromSettings by remember { mutableStateOf(false) }

    LaunchedEffect(state.navigateToSettings) {
        if (state.navigateToSettings) {
            hasReturnedFromSettings = true
            context.openSettings()
        }
    }

    LaunchedEffect(permissionState.canProceed, permissionState.internalState, hasReturnedFromSettings) {
        if (permissionState.canProceed) {
            delay(300L)
            state.eventSink(SplashUiEvent.PermissionResult(true))
            return@LaunchedEffect
        }

        if (permissionState.internalState != null) {
            delay(200L)

            permissionState.showRationale.value = true

            if (hasReturnedFromSettings) {
                state.eventSink(SplashUiEvent.OnResetSettingsNavigation)
                hasReturnedFromSettings = false
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (hasReturnedFromSettings && !permissionState.canProceed) {
                    permissionState.askForPermissions()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
