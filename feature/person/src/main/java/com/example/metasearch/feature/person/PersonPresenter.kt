package com.example.metasearch.feature.person

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.metasearch.feature.screens.PersonScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class PersonPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<PersonUiState> {
    @Composable
    override fun present(): PersonUiState {
        fun handleEvent(event: PersonUiEvent) {
            when (event) {
                is PersonUiEvent.OnTabClick -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return PersonUiState(
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(PersonScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator) : PersonPresenter
    }
}
