package com.metasearch.android.feature.splash

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashSideEffectHandler(
    state: SplashUiState,
    permissionState: MultiplePermissionsState,
    context: Context = LocalContext.current,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(permissionState.allPermissionsGranted, permissionState.shouldShowRationale) {
        if (permissionState.allPermissionsGranted) {
            delay(1000L)
            state.eventSink(SplashUiEvent.PermissionResult(true))
        } else if (permissionState.shouldShowRationale) {
            state.eventSink(SplashUiEvent.PermissionResult(false))
        } else {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                /**
                 * 현재 블록 실행 시 permissionState.allPermissionsGranted가
                 * 자동 갱신된 후, 1번 LaunchedEffect가 반응
                 */
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    LaunchedEffect(state.navigateToSettings) {
        if (state.navigateToSettings) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)

            state.eventSink(SplashUiEvent.OnResetSettingsNavigation)
        }
    }
}
