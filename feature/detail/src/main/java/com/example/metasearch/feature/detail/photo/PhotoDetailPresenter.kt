package com.example.metasearch.feature.detail.photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.metasearch.core.common.utils.handleException
import com.example.metasearch.core.data.api.repository.ImageAnalysisRepository
import com.example.metasearch.feature.screens.FocusingSearchScreen
import com.example.metasearch.feature.screens.GraphDetailScreen
import com.example.metasearch.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class PhotoDetailPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: PhotoDetailScreen,
    private val imageAnalysisRepository: ImageAnalysisRepository,
) : Presenter<PhotoDetailUiState> {

    @CircuitInject(PhotoDetailScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: PhotoDetailScreen,
            navigator: Navigator,
        ): PhotoDetailPresenter
    }

    @Composable
    override fun present(): PhotoDetailUiState {
        val scope = rememberCoroutineScope()
        var isLoading by remember { mutableStateOf(false) }
        var toastMessage by remember { mutableStateOf<String?>(null) }
        val imageUriString by remember { mutableStateOf(screen.imageUriString) }
        var imageDescription by remember { mutableStateOf<String?>(null) }

        fun handleEvent(event: PhotoDetailUiEvent) {
            when (event) {
                is PhotoDetailUiEvent.OnCreateImageDescriptionButtonClick -> {
                    isLoading = true

                    scope.launch {
                        imageAnalysisRepository.getImageDescription(event.imageUriString)
                            .onSuccess { description ->
                                if (description != null) {
                                    imageDescription = description
                                }
                            }
                            .onFailure { exception ->
                                handleException(
                                    exception = exception,
                                    onError = { message ->
                                        toastMessage = message
                                    },
                                )
                            }

                        isLoading = false
                    }
                }

                is PhotoDetailUiEvent.OnGraphButtonClick -> navigator.goTo(
                    GraphDetailScreen(
                        imageUriString = screen.imageUriString,
                    ),
                )

                is PhotoDetailUiEvent.OnFocusingSearchClick -> navigator.goTo(
                    FocusingSearchScreen(
                        imageUriString = event.imageUriString,
                    ),
                )

                is PhotoDetailUiEvent.OnShareImageButtonClick -> TODO()

                PhotoDetailUiEvent.OnBackClick -> navigator.pop()

                PhotoDetailUiEvent.HideToast -> toastMessage = null

                is PhotoDetailUiEvent.ShowToast -> toastMessage = event.message
            }
        }

        return PhotoDetailUiState(
            isLoading = isLoading,
            toastMessage = toastMessage,
            imageUriString = imageUriString,
            imageDescription = imageDescription,
            eventSink = ::handleEvent,
        )
    }
}
