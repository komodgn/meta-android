package com.example.metasearch.feature.search.focusing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import com.example.metasearch.core.data.api.repository.SearchRepository
import com.example.metasearch.core.model.CircleModel
import com.example.metasearch.core.model.SearchResult
import com.example.metasearch.feature.screens.FocusingSearchScreen
import com.example.metasearch.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import java.io.File

class FocusingSearchPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: FocusingSearchScreen,
    private val searchRepository: SearchRepository,
) : Presenter<FocusingSearchUiState> {

    @Composable
    override fun present(): FocusingSearchUiState {
        val coroutineScope = rememberCoroutineScope()

        var isLoading by remember { mutableStateOf(false) }
        var isToastVisible by remember { mutableStateOf(false) }

        val imageUriString by remember { mutableStateOf(screen.imageUriString) }

        var circles by remember { mutableStateOf(listOf<CircleModel>()) }

        var searchResult by remember { mutableStateOf<SearchResult?>(null) }

        fun handleEvent(event: FocusingSearchUiEvent) {
            when (event) {
                FocusingSearchUiEvent.OnSearchClick -> {
                    coroutineScope.launch {
                        isLoading = true
                        val file = File(screen.imageUriString.toUri().path ?: "")

                        searchRepository.focusingSearch(
                            imageFile = file,
                            circles = circles,
                        ).onSuccess {
                            searchResult = it
                            isLoading = false
                        }.onFailure {
                            isLoading = false
                        }
                    }
                }

                is FocusingSearchUiEvent.OnCircleAdded -> circles = circles + event.circle

                is FocusingSearchUiEvent.OnImageClick -> navigator.goTo(PhotoDetailScreen(event.imageUriString))

                FocusingSearchUiEvent.OnCircleResetClick -> {
                    circles = emptyList()
                    searchResult = null
                }

                FocusingSearchUiEvent.OnColorClick -> TODO()

                FocusingSearchUiEvent.OnBackClick -> navigator.pop()

                FocusingSearchUiEvent.HideToast -> isToastVisible = false

                FocusingSearchUiEvent.ShowToast -> isToastVisible = true
            }
        }

        return FocusingSearchUiState(
            isLoading = isLoading,
            isToastVisible = isToastVisible,
            imageUriString = imageUriString,
            circles = circles,
            searchResult = searchResult,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(FocusingSearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: FocusingSearchScreen,
            navigator: Navigator,
        ): FocusingSearchPresenter
    }
}
