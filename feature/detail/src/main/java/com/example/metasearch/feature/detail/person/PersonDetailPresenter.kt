package com.example.metasearch.feature.detail.person

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.metasearch.core.data.api.repository.GalleryRepository
import com.example.metasearch.core.data.api.repository.PersonRepository
import com.example.metasearch.feature.screens.PersonDetailScreen
import com.example.metasearch.feature.screens.PhotoDetailScreen
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

        val isLoading by remember { mutableStateOf(false) }
        val person by personRepository.getPersonById(screen.personId).collectAsState(initial = null)
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
        var showPhotoSelectDialog by rememberRetained { mutableStateOf(false) }

        fun handleEvent(event: PersonDetailUiEvent) {
            when (event) {
                PersonDetailUiEvent.OnHeaderBackClick -> navigator.pop()

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
                            return@launch
                        }

                        personRepository.updatePersonFullInfo(
                            personId = person!!.id,
                            newName = editName,
                            newPhone = editPhone,
                            isHome = editIsHomeDisplay,
                            faceId = editRepresentativeFaceId ?: currentPerson.representativeFaceId,
                        ).onSuccess {
                            if (editName != person?.inputName) {
                                personRepository.changePersonNameOnServer(person!!.inputName, editName)
                            }
                            showEditDialog = false
                        }
                    }
                }

                is PersonDetailUiEvent.OnEditHomeDisplayChange -> editIsHomeDisplay = event.isHomeDisplay

                is PersonDetailUiEvent.OnEditNameChange -> editName = event.name

                is PersonDetailUiEvent.OnEditPhoneChange -> editPhone = event.phone

                PersonDetailUiEvent.OnEditCancel -> showEditDialog = false

                is PersonDetailUiEvent.OnEditThumbnailClick -> {
                    val currentPerson = person ?: return
                    scope.launch {
                        personRepository.updatePersonFullInfo(
                            personId = currentPerson.id,
                            newName = currentPerson.inputName,
                            newPhone = currentPerson.phoneNumber,
                            isHome = currentPerson.isHomeDisplay,
                            faceId = event.faceId,
                        ).onSuccess {
                            showPhotoSelectDialog = false
                        }
                    }
                }

                PersonDetailUiEvent.OnThumbnailClick -> showPhotoSelectDialog = true

                PersonDetailUiEvent.OnPhotoSelectCancel -> showPhotoSelectDialog = false

                is PersonDetailUiEvent.OnGridImageClick -> navigator.goTo(PhotoDetailScreen(event.imageUri.toString()))
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
            showPhotoSelectDialog = showPhotoSelectDialog,
            eventSink = ::handleEvent,
        )
    }
}
