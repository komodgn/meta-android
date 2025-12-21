package com.example.metasearch.feature.splash

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
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
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

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
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
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
