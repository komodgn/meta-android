package com.example.metasearch.feature.search.nls

import androidx.compose.runtime.Composable
import com.example.metasearch.feature.screens.NLSearchScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class NLSearchPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<NLSearchUiState> {

    @Composable
    override fun present(): NLSearchUiState {
        fun handleEvent(event: NLSearchUiEvent) {
            when (event) {
                is NLSearchUiEvent.OnTabClick -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return NLSearchUiState(
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(NLSearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): NLSearchPresenter
    }
}
