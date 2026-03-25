package com.metasearch.android.feature.person

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.core.common.utils.handleException
import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.feature.screens.PersonDetailScreen
import com.metasearch.android.feature.screens.PersonScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AssistedInject
class PersonPresenter(
    @Assisted private val navigator: Navigator,
    private val personRepository: PersonRepository,
) : Presenter<PersonUiState> {

    @CircuitInject(PersonScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): PersonPresenter
    }

    @Composable
    override fun present(): PersonUiState {
        val scope = rememberCoroutineScope()
        var deleteJob by remember { mutableStateOf<Job?>(null) }
        var sideEffect by rememberRetained { mutableStateOf<PersonSideEffect?>(null) }
        var inputPersonNameString by rememberRetained { mutableStateOf("") }
        val allPeople by personRepository.getAllPersons().collectAsState(initial = emptyList())
        val filteredPeople by remember(inputPersonNameString, allPeople) {
            derivedStateOf {
                val list = if (inputPersonNameString.isBlank()) {
                    allPeople
                } else {
                    allPeople.filter { it.inputName.contains(inputPersonNameString, ignoreCase = true) }
                }
                list.toPersistentList()
            }
        }
        var showDeleteDialog by remember { mutableStateOf(false) }
        var pendingDeletePersonId by remember { mutableStateOf<Long?>(null) }
        var pendingDeletePersonName by remember { mutableStateOf("") }

        fun handleEvent(event: PersonUiEvent) {
            when (event) {
                PersonUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is PersonUiEvent.OnInputChange -> {
                    inputPersonNameString = event.inputString
                }

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
                                            sideEffect = PersonSideEffect.ShowToast(message)
                                        },
                                    )
                                    showDeleteDialog = false
                                    pendingDeletePersonId = null
                                }
                        }
                    }
                }

                PersonUiEvent.OnPersonDeleteCancel -> {
                    showDeleteDialog = false
                }

                is PersonUiEvent.OnPersonClick -> {
                    navigator.goTo(PersonDetailScreen(event.personId))
                }

                is PersonUiEvent.OnTabClick -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return PersonUiState(
            showDeleteDialog = showDeleteDialog,
            pendingDeletePersonName = pendingDeletePersonName,
            inputPersonNameString = inputPersonNameString,
            people = filteredPeople,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
