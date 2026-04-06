package com.metasearch.android.feature.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.domain.graph.api.usecase.GetFullGraphUrlUseCase
import com.metasearch.android.domain.graph.api.usecase.GetGraphImageUriUseCase
import com.metasearch.android.feature.screens.GraphScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
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

@AssistedInject
class GraphPresenter(
    @Assisted private val navigator: Navigator,
    private val getFullGraphUrlUseCase: GetFullGraphUrlUseCase,
    private val getGraphImageUriUseCase: GetGraphImageUriUseCase,
) : Presenter<GraphUiState> {

    @CircuitInject(GraphScreen::class, AppScope::class)
    @AssistedFactory
    interface Factory {
        fun create(
            navigator: Navigator,
        ): GraphPresenter
    }

    @Composable
    override fun present(): GraphUiState {
        val coroutineScope = rememberCoroutineScope()
        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Loading) }
        var sideEffect by rememberRetained { mutableStateOf<GraphSideEffect?>(null) }
        var webViewUrl by rememberRetained { mutableStateOf("") }
        var selectedImages by rememberRetained { mutableStateOf<ImmutableList<String>>(persistentListOf()) }
        val maxImages = 10

        LaunchedEffect(Unit) {
            webViewUrl = getFullGraphUrlUseCase()
        }

        fun handleEvent(event: GraphUiEvent) {
            when (event) {
                GraphUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                GraphUiEvent.OnWebLoading -> {
                    uiState = UiState.Loading
                }

                GraphUiEvent.OnWebSuccess -> {
                    uiState = UiState.Success
                }

                is GraphUiEvent.OnWebError -> {
                    uiState = UiState.Error(event.message)
                }

                GraphUiEvent.OnRetry -> {
                    coroutineScope.launch {
                        uiState = UiState.Loading
                        webViewUrl = ""
                        webViewUrl = getFullGraphUrlUseCase()
                    }
                }

                is GraphUiEvent.OnPhotoSelected -> {
                    coroutineScope.launch {
                        val uriString = getGraphImageUriUseCase(event.photoName)

                        if (uriString != null) {
                            if (!selectedImages.contains(uriString)) {
                                selectedImages = (listOf(uriString) + selectedImages).take(maxImages).toPersistentList()
                            }
                        } else {
                            sideEffect = GraphSideEffect.ShowToast(UiText.StringResource(R.string.graph_screen_image_not_found_error))
                        }
                    }
                }

                is GraphUiEvent.OnImageClick -> {
                    navigator.goTo(PhotoDetailScreen(event.uriString))
                }

                is GraphUiEvent.OnTabClick -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return GraphUiState(
            uiState = uiState,
            webViewUrl = webViewUrl,
            selectedImages = selectedImages,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
