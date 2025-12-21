package com.example.metasearch.feature.detail.person

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.metasearch.feature.screens.PersonDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class PersonDetailPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
//    @Assisted private val screen: PersonDetailScreen,
) : Presenter<PersonDetailUiState> {
    @Composable
    override fun present(): PersonDetailUiState {
        val isLoading by remember { mutableStateOf(false) }

        fun handleEvent(event: PersonDetailUiEvent) {
            when (event) {
                PersonDetailUiEvent.OnHeaderBackClick -> navigator.pop()
            }
        }

        return PersonDetailUiState(
            isLoading = isLoading,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(PersonDetailScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
//            screen: PersonDetailScreen,
            navigator: Navigator,
        ): PersonDetailPresenter
    }
}
