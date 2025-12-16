package com.example.metasearch.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.metasearch.feature.screens.HomeScreen
import com.example.metasearch.feature.screens.SplashScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.delay

class SplashPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
): Presenter<SplashUiState> {

    @Composable
    override fun present(): SplashUiState {
        fun handleEvent(event: SplashUiEvent) {
            when (event) {
                SplashUiEvent.OnNavigationToNextScreen -> {
                    navigator.resetRoot(HomeScreen)
                }
            }
        }

        LaunchedEffect(Unit) {
            delay(2000)

            handleEvent(SplashUiEvent.OnNavigationToNextScreen)
        }

        return SplashUiState(
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SplashPresenter
    }
}
