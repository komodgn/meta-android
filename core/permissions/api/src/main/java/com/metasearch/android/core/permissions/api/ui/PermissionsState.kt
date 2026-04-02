package com.metasearch.android.core.permissions.api.ui

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.collections.immutable.ImmutableList

@Stable
@OptIn(ExperimentalPermissionsApi::class)
class PermissionsState(
    val permissions: ImmutableList<String>,
    val showRationale: MutableState<Boolean> = mutableStateOf(false),
    val onGoToSettings: () -> Unit = {},
) {
    private val _internalState = mutableStateOf<MultiplePermissionsState?>(null)
    var internalState: MultiplePermissionsState?
        get() = _internalState.value
        set(value) { _internalState.value = value }

    val allPermissionsGranted: Boolean
        get() = internalState?.allPermissionsGranted ?: false

    val canProceed: Boolean by derivedStateOf {
        val state = _internalState.value ?: return@derivedStateOf false

        val hasPhotoAccess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            state.permissions.any {
                (
                    it.permission == Manifest.permission.READ_MEDIA_IMAGES ||
                        it.permission == Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                    ) && it.status.isGranted
            }
        } else {
            state.permissions.any {
                (
                    it.permission == Manifest.permission.READ_MEDIA_IMAGES ||
                        it.permission == Manifest.permission.READ_EXTERNAL_STORAGE
                    ) && it.status.isGranted
            }
        }

        val otherPermissionsGranted = state.permissions
            .filter {
                it.permission != Manifest.permission.READ_MEDIA_IMAGES &&
                    it.permission != Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED &&
                    it.permission != Manifest.permission.READ_EXTERNAL_STORAGE
            }
            .all { it.status.isGranted }

        hasPhotoAccess && otherPermissionsGranted
    }

    @Composable
    fun Content() {
        internalState = rememberMultiplePermissionsState(permissions)

        if (showRationale.value) {
            PermissionDialog(state = this)
        }
    }

    fun askForPermissions() {
        val state = internalState ?: return
        if (canProceed) return

        state.launchMultiplePermissionRequest()

        showRationale.value = true
    }

    fun launchSystemRequest() {
        internalState?.launchMultiplePermissionRequest()
    }

    companion object {
        @Composable
        fun rememberPermissionsState(
            permissions: ImmutableList<String>,
            onGoToSettings: () -> Unit,
        ): PermissionsState {
            val state = remember(permissions, onGoToSettings) {
                PermissionsState(
                    permissions,
                    onGoToSettings = onGoToSettings,
                )
            }
            state.Content()
            return state
        }
    }
}
