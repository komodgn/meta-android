package com.example.metasearch.feature.detail.photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

class PhotoDetailPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: PhotoDetailScreen,
) : Presenter<PhotoDetailUiState> {

    @Composable
    override fun present(): PhotoDetailUiState {
        var isLoading by remember { mutableStateOf(false) }
        val imageUriString by remember { mutableStateOf(screen.imageUriString) }

        fun handleEvent(event: PhotoDetailUiEvent) {
            when (event) {
                is PhotoDetailUiEvent.OnCreateImageDescriptionButtonClick -> TODO()

                is PhotoDetailUiEvent.OnGraphButtonClick -> navigator.goTo(
                    GraphDetailScreen(
//                        imageUriString = screen.imageUriString,
                        imageUriString = "https://www.google.com",
                    ),
                )

                is PhotoDetailUiEvent.OnFocusingSearchClick -> navigator.goTo(
                    FocusingSearchScreen(
                        imageUriString = event.imageUriString,
                    ),
                )

                is PhotoDetailUiEvent.OnShareImageButtonClick -> TODO()

                PhotoDetailUiEvent.OnBackClick -> navigator.pop()
            }
        }

        return PhotoDetailUiState(
            isLoading = isLoading,
            imageUriString = imageUriString,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(PhotoDetailScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: PhotoDetailScreen,
            navigator: Navigator,
        ): PhotoDetailPresenter
    }
}
