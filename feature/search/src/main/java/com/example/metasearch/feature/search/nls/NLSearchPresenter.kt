package com.example.metasearch.feature.search.nls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.metasearch.core.common.utils.handleException
import com.example.metasearch.core.data.api.repository.SearchRepository
import com.example.metasearch.feature.screens.NLSearchScreen
import com.example.metasearch.feature.screens.PhotoDetailScreen
import com.example.metasearch.feature.search.R
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
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
        var isLoading by remember { mutableStateOf(false) }
        var toastMessage by remember { mutableStateOf<String?>(null) }
        var inputString by remember { mutableStateOf("") }
        var resultImages by remember { mutableStateOf<List<String>>(emptyList()) }

        val emptyResultMessage = stringResource(R.string.search_screen_empty_result_message)

        fun handleEvent(event: NLSearchUiEvent) {
            when (event) {
                is NLSearchUiEvent.OnInputChange -> inputString = event.inputString

                is NLSearchUiEvent.OnNLSearchClick -> {
                    if (inputString.isBlank()) return
                    isLoading = true

                    scope.launch {
                        searchRepository.nlSearch(inputString)
                            .onSuccess { result ->
                                if (result.matchedUris.isEmpty()) {
                                    resultImages = emptyList()
                                    toastMessage = emptyResultMessage
                                } else {
                                    resultImages = result.matchedUris
                                }
                            }.onFailure { exception ->
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

                is NLSearchUiEvent.OnImageClick -> {
                    navigator.goTo(PhotoDetailScreen(event.imageUriString))
                }

                is NLSearchUiEvent.OnTabClick -> navigator.resetRoot(event.screen)

                is NLSearchUiEvent.ShowToast -> toastMessage = event.message

                NLSearchUiEvent.HideToast -> toastMessage = null
            }
        }

        return NLSearchUiState(
            isLoading = isLoading,
            toastMessage = toastMessage,
            inputString = inputString,
            resultImages = resultImages,
            eventSink = ::handleEvent,
        )
    }
}
