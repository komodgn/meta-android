package com.example.metasearch.feature.person

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.metasearch.core.common.utils.handleException
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PersonPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val personRepository: PersonRepository,
) : Presenter<PersonUiState> {

    @CircuitInject(PersonScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): PersonPresenter
    }

    @Composable
    override fun present(): PersonUiState {
        val scope = rememberCoroutineScope()

        var deleteJob by remember { mutableStateOf<Job?>(null) }

        var showToast by remember { mutableStateOf(false) }
        var toastMessage by remember { mutableStateOf("") }
        var showDeleteDialog by remember { mutableStateOf(false) }
        var pendingDeletePersonId by remember { mutableStateOf<Long?>(null) }
        var pendingDeletePersonName by remember { mutableStateOf("") }
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
                }

                is PersonUiEvent.OnPersonSearchClick -> TODO()

                is PersonUiEvent.OnPersonDeleteClick -> {
                    val target = allPeople.find { it.id == event.personId }
                    pendingDeletePersonId = event.personId
                    pendingDeletePersonName = target?.inputName ?: ""
                    showDeleteDialog = true
                }

                is PersonUiEvent.OnPersonDeleteConfirm -> {
                    if (deleteJob?.isActive == true) return

                    val personToDelete = allPeople.find { it.id == pendingDeletePersonId }
                    personToDelete?.let { person ->
                        deleteJob = scope.launch {
                            personRepository.deleteAnalyzedPerson(person)
                                .onSuccess {
                                    showDeleteDialog = false
                                    pendingDeletePersonId = null
                                }
                                .onFailure { exception ->
                                    handleException(
                                        exception = exception,
                                        onError = { message ->
                                            toastMessage = message
                                            showToast = true
                                        },
                                    )
                                    showDeleteDialog = false
                                    pendingDeletePersonId = null
                                }
                        }
                    }
                }

                PersonUiEvent.OnPersonDeleteCancel -> showDeleteDialog = false

                is PersonUiEvent.OnPersonClick -> navigator.goTo(PersonDetailScreen(event.personId))

                is PersonUiEvent.OnTabClick -> {
                    navigator.resetRoot(event.screen)
                }

                PersonUiEvent.HideToast -> showToast = false
            }
        }

        return PersonUiState(
            toastMessage = toastMessage,
            showToast = showToast,
            showDeleteDialog = showDeleteDialog,
            pendingDeletePersonName = pendingDeletePersonName,
            inputPersonNameString = inputPersonNameString,
            people = filteredPeople,
            eventSink = ::handleEvent,
        )
    }
}
