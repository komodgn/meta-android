package com.example.metasearch.feature.person

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.metasearch.core.data.api.repository.PersonRepository
import com.example.metasearch.feature.screens.PersonDetailScreen
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
    private val personRepository: PersonRepository,
) : Presenter<PersonUiState> {

    @Composable
    override fun present(): PersonUiState {
        var inputPersonNameString by remember { mutableStateOf("") }
        val allPeople by personRepository.getAllPersons().collectAsState(initial = emptyList())

        val filteredPeople = remember(inputPersonNameString, allPeople) {
            if (inputPersonNameString.isBlank()) {
                allPeople
            } else {
                allPeople.filter { person ->
                    person.inputName.contains(inputPersonNameString, ignoreCase = true) ||
                        person.name.contains(inputPersonNameString, ignoreCase = true)
                }
            }
        }

        fun handleEvent(event: PersonUiEvent) {
            when (event) {
                is PersonUiEvent.OnInputChange -> {
                    inputPersonNameString = event.inputString
                    Log.d("PersonPresenter", "Input: ${event.inputString}")
                }

                is PersonUiEvent.OnPersonSearchClick -> TODO()

                is PersonUiEvent.OnPersonClick -> navigator.goTo(PersonDetailScreen(event.personId))

                is PersonUiEvent.OnTabClick -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return PersonUiState(
            inputPersonNameString = inputPersonNameString,
            people = filteredPeople,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(PersonScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): PersonPresenter
    }
}
