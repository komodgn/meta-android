package com.metasearch.android.feature.search.focusing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.common.utils.handleException
import com.metasearch.android.data.domain.Circle
import com.metasearch.android.data.domain.DragSearchResult
import com.metasearch.android.domain.file.api.repository.FileRepository
import com.metasearch.android.domain.search.api.usecase.DragSearchUseCase
import com.metasearch.android.feature.screens.FocusingSearchScreen
import com.metasearch.android.feature.screens.GraphDetailScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.metasearch.android.feature.search.R
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AssistedInject
class FocusingSearchPresenter(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: FocusingSearchScreen,
    private val dragSearchUseCase: DragSearchUseCase,
    private val fileRepository: FileRepository,
) : Presenter<FocusingSearchUiState> {

    @CircuitInject(FocusingSearchScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: FocusingSearchScreen,
            navigator: Navigator,
        ): FocusingSearchPresenter
    }

    @Composable
    override fun present(): FocusingSearchUiState {
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
            mutableStateOf<PersistentList<Circle>>(persistentListOf())
        }
        var searchResult by rememberRetained {
            mutableStateOf<DragSearchResult?>(null)
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
                        fileRepository.createTempFileFromUri(screen.imageUriString)
                            .onSuccess { file ->
                                try {
                                    dragSearchUseCase(file, circles)
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
                                                exception,
                                                onError = { sideEffect = FocusingSearchSideEffect.ShowToast(it) },
                                            )
                                        }
                                } finally {
                                    fileRepository.deleteFile(file)
                                }
                            }
                            .onFailure { exception ->
                                sideEffect = FocusingSearchSideEffect.ShowToast(
                                    message = UiText.StringResource(R.string.focusing_search_screen_error_prepare_image_failed),
                                )
                            }

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

                FocusingSearchUiEvent.OnColorClick -> {
                    sideEffect = FocusingSearchSideEffect.ShowToast(
                        UiText.DynamicString("Implement your custom action!"),
                    )
                }

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
