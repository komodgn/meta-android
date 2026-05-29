package com.metasearch.android.feature.person_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.domain.person.api.usecase.CheckNameExistsUseCase
import com.metasearch.android.domain.person.api.usecase.GetPersonDetailUseCase
import com.metasearch.android.domain.person.api.usecase.GetPersonPhotosUseCase
import com.metasearch.android.domain.person.api.usecase.UpdatePersonInfoUseCase
import com.metasearch.android.domain.person.api.usecase.UpdateRepresentativeFaceUseCase
import com.metasearch.android.feature.screens.PersonDetailScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Suppress("LongParameterList")
@AssistedInject
class PersonDetailPresenter(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: PersonDetailScreen,
    private val getPersonDetailUseCase: GetPersonDetailUseCase,
    private val getPersonPhotosUseCase: GetPersonPhotosUseCase,
    private val checkNameExistsUseCase: CheckNameExistsUseCase,
    private val updatePersonInfoUseCase: UpdatePersonInfoUseCase,
    private val updateRepresentativeFaceUseCase: UpdateRepresentativeFaceUseCase,
) : Presenter<PersonDetailUiState> {

    @CircuitInject(PersonDetailScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: PersonDetailScreen,
            navigator: Navigator,
        ): PersonDetailPresenter
    }

    @Composable
    override fun present(): PersonDetailUiState {
        val scope = rememberCoroutineScope()

        var isLoading by remember { mutableStateOf(false) }
        var currentPersonId by remember { mutableLongStateOf(screen.personId) }
        val person by getPersonDetailUseCase(currentPersonId).collectAsState(initial = null)
        val photoUris by produceState(initialValue = persistentListOf(), key1 = person?.inputName) {
            val name = person?.inputName ?: return@produceState
            getPersonPhotosUseCase(name).onSuccess { uris ->
                value = uris.toPersistentList()
            }
        }
        var showEditDialog by rememberRetained { mutableStateOf(false) }
        var editName by rememberRetained { mutableStateOf("") }
        var editPhone by rememberRetained { mutableStateOf("") }
        var editIsHomeDisplay by rememberRetained { mutableStateOf(false) }
        var editRepresentativeFaceId by rememberRetained { mutableStateOf<Long?>(null) }
        var showMergeConfirmDialog by rememberRetained { mutableStateOf(false) }
        var showPhotoSelectDialog by rememberRetained { mutableStateOf(false) }

        var isInitialized by remember { mutableStateOf(false) }

        LaunchedEffect(person) {
            if (person != null) {
                isInitialized = true
            } else if (isInitialized && !isLoading) {
                navigator.pop()
            }
        }

        suspend fun performSave() {
            val currentPerson = person ?: return
            updatePersonInfoUseCase(
                personId = currentPerson.id,
                newName = editName,
                newPhone = editPhone,
                isHome = editIsHomeDisplay,
                faceId = editRepresentativeFaceId ?: currentPerson.representativeFaceId,
            ).onSuccess { finalPersonId ->
                showEditDialog = false
                if (finalPersonId != currentPersonId) {
                    currentPersonId = finalPersonId
                }
            }
        }

        fun handleEvent(event: PersonDetailUiEvent) {
            when (event) {
                PersonDetailUiEvent.OnHeaderBackClick -> {
                    navigator.pop()
                }

                PersonDetailUiEvent.OnMenuClick -> {
                    person?.let {
                        editName = it.inputName
                        editPhone = it.phoneNumber
                        editIsHomeDisplay = it.isHomeDisplay
                        editRepresentativeFaceId = it.representativeFaceId
                        showEditDialog = true
                    }
                }

                is PersonDetailUiEvent.OnEditSaveClick -> {
                    scope.launch {
                        val currentPerson = person ?: return@launch
                        if (editName != currentPerson.inputName && checkNameExistsUseCase(editName)) {
                            showEditDialog = false
                            showMergeConfirmDialog = true
                        } else {
                            performSave()
                        }
                    }
                }

                is PersonDetailUiEvent.OnEditHomeDisplayChange -> {
                    editIsHomeDisplay = event.isHomeDisplay
                }

                is PersonDetailUiEvent.OnEditNameChange -> {
                    editName = event.name
                }

                is PersonDetailUiEvent.OnEditPhoneChange -> {
                    editPhone = event.phone
                }

                PersonDetailUiEvent.OnEditCancel -> {
                    showEditDialog = false
                }

                PersonDetailUiEvent.OnConfirmMergeSave -> {
                    scope.launch {
                        isLoading = true
                        try {
                            performSave()
                            showMergeConfirmDialog = false
                        } finally {
                            isLoading = false
                        }
                    }
                }

                PersonDetailUiEvent.OnDismissMergeDialog -> {
                    showMergeConfirmDialog = false
                }

                is PersonDetailUiEvent.OnEditThumbnailClick -> {
                    val currentPerson = person ?: return
                    scope.launch {
                        updateRepresentativeFaceUseCase(
                            personId = currentPerson.id,
                            faceId = event.faceId,
                        ).onSuccess {
                            showPhotoSelectDialog = false
                        }
                    }
                }

                PersonDetailUiEvent.OnThumbnailClick -> {
                    showPhotoSelectDialog = true
                }

                PersonDetailUiEvent.OnPhotoSelectCancel -> {
                    showPhotoSelectDialog = false
                }

                is PersonDetailUiEvent.OnGridImageClick -> {
                    navigator.goTo(PhotoDetailScreen(event.imageUri.toString()))
                }
            }
        }

        return PersonDetailUiState(
            isLoading = isLoading,
            person = person,
            photoUris = photoUris,
            showEditDialog = showEditDialog,
            editName = editName,
            editPhone = editPhone,
            editIsHomeDisplay = editIsHomeDisplay,
            editRepresentativeFaceId = editRepresentativeFaceId,
            showMergeConfirmDialog = showMergeConfirmDialog,
            showPhotoSelectDialog = showPhotoSelectDialog,
            eventSink = ::handleEvent,
        )
    }
}
