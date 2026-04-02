package com.metasearch.android.feature.splash

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.metasearch.android.feature.screens.HomeScreen
import com.metasearch.android.feature.screens.SplashScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@AssistedInject
class SplashPresenter(
    @Assisted private val navigator: Navigator,
) : Presenter<SplashUiState> {

    @CircuitInject(SplashScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SplashPresenter
    }

    @Composable
    override fun present(): SplashUiState {
        var navigateToSettings by remember { mutableStateOf(false) }
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34+
            persistentListOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33
            listOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
            )
        } else {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
            )
        }.toImmutableList()

        fun goToNextScreen() {
            navigator.resetRoot(HomeScreen)
        }

        fun onPermissionsResult(allGranted: Boolean) {
            if (allGranted) {
                goToNextScreen()
            }
        }

        fun handleEvent(event: SplashUiEvent) {
            when (event) {
                is SplashUiEvent.PermissionResult -> {
                    onPermissionsResult(event.allGranted)
                }

                SplashUiEvent.OnConfirmSettings -> {
                    navigateToSettings = true
                }

                SplashUiEvent.OnResetSettingsNavigation -> {
                    navigateToSettings = false
                }
            }
        }

        return SplashUiState(
            permissions = permissions,
            navigateToSettings = navigateToSettings,
            eventSink = ::handleEvent,
        )
    }
}
