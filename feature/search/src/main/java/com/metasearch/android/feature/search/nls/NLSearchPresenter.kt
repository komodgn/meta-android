package com.metasearch.android.feature.search.nls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.common.utils.handleException
import com.metasearch.android.core.data.api.repository.SearchRepository
import com.metasearch.android.feature.screens.NLSearchScreen
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class NLSearchPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val searchRepository: SearchRepository,
) : Presenter<NLSearchUiState> {

    @CircuitInject(NLSearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): NLSearchPresenter
    }

    @Composable
    override fun present(): NLSearchUiState {
        val scope = rememberCoroutineScope()
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<NLSearchSideEffect?>(null) }
        var inputString by rememberRetained { mutableStateOf("") }
        var resultImages by rememberRetained { mutableStateOf<ImmutableList<String>>(persistentListOf()) }

        fun handleEvent(event: NLSearchUiEvent) {
            when (event) {
                NLSearchUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is NLSearchUiEvent.OnInputChange -> {
                    inputString = event.inputString
                }

                is NLSearchUiEvent.OnNLSearchClick -> {
                    inputString = event.inputString

                    if (inputString.isBlank()) return
                    isLoading = true

                    scope.launch {
                        searchRepository.nlSearch(inputString)
                            .onSuccess { result ->
                                if (result.matchedUris.isEmpty()) {
                                    resultImages = persistentListOf()
                                    sideEffect = NLSearchSideEffect.ShowToast(
                                        message = UiText.StringResource(R.string.search_screen_empty_result_message),
                                    )
                                } else {
                                    resultImages = result.matchedUris.toPersistentList()
                                }
                            }.onFailure { exception ->
                                handleException(
                                    exception = exception,
                                    onError = { message ->
                                        sideEffect = NLSearchSideEffect.ShowToast(
                                            message = UiText.DynamicString(message),
                                        )
                                    },
                                )
                            }
                        isLoading = false
                    }
                }

                is NLSearchUiEvent.OnImageClick -> {
                    navigator.goTo(PhotoDetailScreen(event.imageUriString))
                }

                is NLSearchUiEvent.OnTabClick -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return NLSearchUiState(
            isLoading = isLoading,
            inputString = inputString,
            resultImages = resultImages,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
