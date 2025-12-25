package com.example.metasearch.feature.detail.person

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.metasearch.core.data.api.repository.GalleryRepository
import com.example.metasearch.core.data.api.repository.PersonRepository
import com.example.metasearch.feature.screens.PersonDetailScreen
import com.example.metasearch.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class PersonDetailPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: PersonDetailScreen,
    private val personRepository: PersonRepository,
    private val galleryRepository: GalleryRepository,
) : Presenter<PersonDetailUiState> {

    @Composable
    override fun present(): PersonDetailUiState {
        val isLoading by remember { mutableStateOf(false) }
        val person by personRepository.getPersonById(screen.personId).collectAsState(initial = null)
        var photoUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

        LaunchedEffect(person?.inputName) {
            val nameTosearch = person?.inputName ?: return@LaunchedEffect

            personRepository.getPersonPhotoNames(nameTosearch).onSuccess { photoNames ->
                photoUris = galleryRepository.findMatchedUris(photoNames)
            }
        }

        fun handleEvent(event: PersonDetailUiEvent) {
            when (event) {
                PersonDetailUiEvent.OnHeaderBackClick -> navigator.pop()

                is PersonDetailUiEvent.OnGridImageClick -> navigator.goTo(PhotoDetailScreen(event.imageUri.toString()))
            }
        }

        return PersonDetailUiState(
            isLoading = isLoading,
            person = person,
            photoUris = photoUris,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(PersonDetailScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: PersonDetailScreen,
            navigator: Navigator,
        ): PersonDetailPresenter
    }
}
