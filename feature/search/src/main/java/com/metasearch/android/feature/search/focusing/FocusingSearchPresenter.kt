package com.metasearch.android.feature.search.focusing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.metasearch.android.core.common.extensions.toFile
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.common.utils.handleException
import com.metasearch.android.core.data.api.repository.SearchRepository
import com.metasearch.android.core.model.CircleModel
import com.metasearch.android.core.model.SearchResult
import com.metasearch.android.feature.screens.FocusingSearchScreen
import com.metasearch.android.feature.screens.GraphDetailScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.metasearch.android.feature.search.R
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
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

        var sideEffect by rememberRetained {
            mutableStateOf<FocusingSearchSideEffect?>(
                FocusingSearchSideEffect.ShowToast(
                    message = UiText.StringResource(R.string.focusing_search_screen_toast_guide),
                ),
            )
        }
        var isLoading by remember { mutableStateOf(false) }
        var searchJob by remember { mutableStateOf<Job?>(null) }

        val imageUriString by rememberRetained {
            mutableStateOf(screen.imageUriString)
        }
        var circles by rememberRetained {
            mutableStateOf<PersistentList<CircleModel>>(persistentListOf())
        }
        var searchResult by rememberRetained {
            mutableStateOf<SearchResult?>(null)
        }

        fun handleEvent(event: FocusingSearchUiEvent) {
            when (event) {
                FocusingSearchUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                FocusingSearchUiEvent.OnSearchClick -> {
                    if (circles.isEmpty()) {
                        sideEffect = FocusingSearchSideEffect.ShowToast(
                            message = UiText.StringResource(R.string.focusing_search_screen_toast_error_min_circles),
                        )
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
                                    sideEffect = FocusingSearchSideEffect.ShowToast(
                                        message = UiText.StringResource(R.string.search_screen_empty_result_message),
                                    )
                                } else {
                                    searchResult = result
                                }
                            }.onFailure { exception ->
                                handleException(
                                    exception = exception,
                                    onError = { message ->
                                        sideEffect = FocusingSearchSideEffect.ShowToast(message)
                                    },
                                )
                            }

                        file.delete()
                        isLoading = false
                    }
                }

                is FocusingSearchUiEvent.OnCircleAdded -> {
                    if (circles.size >= 3) {
                        sideEffect = FocusingSearchSideEffect.ShowToast(
                            UiText.StringResource(R.string.focusing_search_screen_toast_error_max_circles),
                        )
                    } else {
                        circles = circles.add(event.circle)
                    }
                }

                is FocusingSearchUiEvent.OnImageClick -> {
                    navigator.goTo(PhotoDetailScreen(event.imageUriString))
                }

                FocusingSearchUiEvent.OnCircleResetClick -> {
                    circles = persistentListOf()
                    searchResult = null
                }

                FocusingSearchUiEvent.OnColorClick -> TODO()

                FocusingSearchUiEvent.OnBackClick -> {
                    searchJob?.cancel()
                    navigator.pop()
                }

                is FocusingSearchUiEvent.OnMoreClick -> {
                    navigator.goTo(GraphDetailScreen(event.categoryName))
                }
            }
        }

        return FocusingSearchUiState(
            isLoading = isLoading,
            imageUriString = imageUriString,
            circles = circles,
            searchResult = searchResult,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
