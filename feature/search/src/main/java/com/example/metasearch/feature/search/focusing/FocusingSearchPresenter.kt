package com.example.metasearch.feature.search.focusing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.example.metasearch.core.common.extensions.toFile
import com.example.metasearch.core.common.utils.handleException
import com.example.metasearch.core.data.api.repository.SearchRepository
import com.example.metasearch.core.model.CircleModel
import com.example.metasearch.core.model.SearchResult
import com.example.metasearch.feature.screens.FocusingSearchScreen
import com.example.metasearch.feature.screens.PhotoDetailScreen
import com.example.metasearch.feature.search.R
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FocusingSearchPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: FocusingSearchScreen,
    private val searchRepository: SearchRepository,
) : Presenter<FocusingSearchUiState> {

    @CircuitInject(FocusingSearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: FocusingSearchScreen,
            navigator: Navigator,
        ): FocusingSearchPresenter
    }

    @Composable
    override fun present(): FocusingSearchUiState {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        var isLoading by remember { mutableStateOf(false) }
        var searchJob by remember { mutableStateOf<Job?>(null) }
        val toastInit = stringResource(R.string.focusing_search_screen_toast_guide)
        val errorMinCircles = stringResource(R.string.focusing_search_screen_toast_error_min_circles)
        val errorMaxCircles = stringResource(R.string.focusing_search_screen_toast_error_max_circles)
        var toastMessage by remember { mutableStateOf<String?>(toastInit) }

        val imageUriString by remember { mutableStateOf(screen.imageUriString) }
        var circles by remember { mutableStateOf(listOf<CircleModel>()) }
        var searchResult by remember { mutableStateOf<SearchResult?>(null) }
        val emptyResultMessage = stringResource(R.string.search_screen_empty_result_message)

        fun handleEvent(event: FocusingSearchUiEvent) {
            when (event) {
                FocusingSearchUiEvent.OnSearchClick -> {
                    if (circles.isEmpty()) {
                        handleEvent(FocusingSearchUiEvent.ShowToast(errorMinCircles))
                        return
                    }

                    searchJob?.cancel()
                    
                    isLoading = true

                    searchJob = coroutineScope.launch {
                        val uri = screen.imageUriString.toUri()
                        val file = uri.toFile(context)

                        searchRepository.focusingSearch(file, circles)
                            .onSuccess { result ->
                                if (result.groups.isEmpty()) {
                                    toastMessage = emptyResultMessage
                                } else {
                                    searchResult = result
                                }
                            }.onFailure { exception ->
                                handleException(
                                    exception = exception,
                                    onError = { message -> toastMessage = message },
                                )
                            }

                        file.delete()
                        isLoading = false
                    }
                }

                is FocusingSearchUiEvent.OnCircleAdded -> {
                    if (circles.size >= 3) {
                        handleEvent(FocusingSearchUiEvent.ShowToast(errorMaxCircles))
                    } else {
                        circles = circles + event.circle
                    }
                }

                is FocusingSearchUiEvent.OnImageClick -> navigator.goTo(PhotoDetailScreen(event.imageUriString))

                FocusingSearchUiEvent.OnCircleResetClick -> {
                    circles = emptyList()
                    searchResult = null
                }

                FocusingSearchUiEvent.OnColorClick -> TODO()

                FocusingSearchUiEvent.OnBackClick -> navigator.pop()

                FocusingSearchUiEvent.HideToast -> toastMessage = null

                is FocusingSearchUiEvent.ShowToast -> toastMessage = event.message
            }
        }

        return FocusingSearchUiState(
            isLoading = isLoading,
            toastMessage = toastMessage,
            imageUriString = imageUriString,
            circles = circles,
            searchResult = searchResult,
            eventSink = ::handleEvent,
        )
    }
}
