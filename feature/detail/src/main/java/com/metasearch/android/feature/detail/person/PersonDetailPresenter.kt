package com.metasearch.android.feature.detail.person

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
import com.metasearch.android.core.data.api.repository.GalleryRepository
import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.feature.screens.PersonDetailScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class PersonDetailPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: PersonDetailScreen,
    private val personRepository: PersonRepository,
    private val galleryRepository: GalleryRepository,
) : Presenter<PersonDetailUiState> {

    @CircuitInject(PersonDetailScreen::class, ActivityRetainedComponent::class)
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
        val person by personRepository.getPersonById(currentPersonId).collectAsState(initial = null)
        val photoUris by produceState(initialValue = emptyList(), key1 = person?.inputName) {
            val nameToSearch = person?.inputName
            if (nameToSearch != null) {
                personRepository.getPersonPhotoNames(nameToSearch).onSuccess { photoNames ->
                    value = galleryRepository.findMatchedUris(photoNames)
                }
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

        suspend fun savePersonInfo() {
            val currentPerson = person ?: return

            personRepository.updatePersonFullInfo(
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
                    val currentPerson = person ?: return
                    scope.launch {
                        if (editName != person?.inputName && personRepository.isNameExists(editName)) {
                            showMergeConfirmDialog = true
                        } else {
                            savePersonInfo()
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
                        savePersonInfo()
                        showMergeConfirmDialog = false
                        isLoading = false
                    }
                }

                PersonDetailUiEvent.OnDismissMergeDialog -> {
                    showMergeConfirmDialog = false
                }

                is PersonDetailUiEvent.OnEditThumbnailClick -> {
                    val currentPerson = person ?: return
                    scope.launch {
                        personRepository.updateRepresentativeFace(
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
