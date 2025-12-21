package com.example.metasearch.feature.search.nls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.metasearch.core.data.api.repository.SearchRepository
import com.example.metasearch.feature.screens.NLSearchScreen
import com.example.metasearch.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import com.example.metasearch.feature.search.R

class NLSearchPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val searchRepository: SearchRepository,
) : Presenter<NLSearchUiState> {

    @Composable
    override fun present(): NLSearchUiState {
        val scope = rememberCoroutineScope()
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }
        var inputString by remember { mutableStateOf("") }
        var resultImages by remember { mutableStateOf<List<String>>(emptyList()) }

        val nlSearchFailed = stringResource(R.string.nl_search_screen_error_message)

        fun handleEvent(event: NLSearchUiEvent) {
            when (event) {
                is NLSearchUiEvent.OnInputChange -> {
                    inputString = event.inputString
                }

                is NLSearchUiEvent.OnNLSearchClick -> {
                    if (inputString.isBlank()) return

                    isLoading = true

                    scope.launch {
                        try {
                            searchRepository.nlSearch(
                                query = inputString,
                            ).onSuccess {
                                resultImages = it.matchedUris
                            }.onFailure {
                                errorMessage = nlSearchFailed
                            }
                        } finally {
                            isLoading = false
                        }
                    }
                }

                NLSearchUiEvent.OnDialogCloseButtonClick -> {
                    errorMessage = ""
                }

                is NLSearchUiEvent.OnImageClick -> {
                    navigator.goTo(
                        PhotoDetailScreen(
                            event.imageUriString,
                        )
                    )
                }

                is NLSearchUiEvent.OnTabClick -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return NLSearchUiState(
            isLoading = isLoading,
            errorMessage = errorMessage,
            inputString = inputString,
            resultImages = resultImages,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(NLSearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): NLSearchPresenter
    }
}
