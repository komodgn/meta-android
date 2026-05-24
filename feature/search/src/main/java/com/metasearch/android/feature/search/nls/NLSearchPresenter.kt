package com.metasearch.android.feature.search.nls

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.core.common.utils.UiText.StringResource
import com.metasearch.android.core.common.utils.handleException
import com.metasearch.android.domain.search.api.repository.ModelRepository
import com.metasearch.android.domain.search.api.repository.SearchRepository
import com.metasearch.android.domain.search.api.usecase.NLSearchUseCase
import com.metasearch.android.feature.screens.NLSearchScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.metasearch.android.feature.search.R
import com.metasearch.android.feature.search.nls.NLSearchSideEffect.ShowToast
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

private const val TAG = "NLSearchPresenter"

@AssistedInject
class NLSearchPresenter(
    @Assisted private val navigator: Navigator,
    private val nlSearchUseCase: NLSearchUseCase,
    private val searchRepository: SearchRepository,
    private val modelRepository: ModelRepository,
) : Presenter<NLSearchUiState> {

    @CircuitInject(NLSearchScreen::class, AppScope::class)
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

        val isLocalEngineReady by remember(modelRepository.getAllModels()) {
            derivedStateOf {
                val model = modelRepository.getModel("Gemma-4-E2B-it")
                model != null && searchRepository.isLocalModelAvailable(model)
            }
        }
        var isLocalSearchEnabled by remember { mutableStateOf(false) }
        Log.d(TAG, isLocalSearchEnabled.toString())
        val effectiveLocalSearchEnabled = isLocalEngineReady && isLocalSearchEnabled

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
                        nlSearchUseCase(inputString, isLocalSearchEnabled)
                            .onSuccess { result ->
                                if (result.matchedUris.isEmpty()) {
                                    resultImages = persistentListOf()
                                    sideEffect = ShowToast(
                                        message = StringResource(R.string.search_screen_empty_result_message),
                                    )
                                } else {
                                    resultImages = result.matchedUris.toPersistentList()
                                }
                            }.onFailure { exception ->
                                handleException(exception, onError = { sideEffect = ShowToast(it) })
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

                is NLSearchUiEvent.OnToggleLocalSearch -> {
                    if (isLocalEngineReady) {
                        isLocalSearchEnabled = event.isEnabled
                    }
                }
            }
        }

        return NLSearchUiState(
            isLoading = isLoading,
            inputString = inputString,
            resultImages = resultImages,
            isLocalEngineReady = isLocalEngineReady,
            isLocalSearchEnabled = effectiveLocalSearchEnabled,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
