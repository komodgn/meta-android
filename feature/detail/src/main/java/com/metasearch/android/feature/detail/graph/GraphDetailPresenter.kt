package com.metasearch.android.feature.detail.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.domain.graph.api.usecase.GetDetailGraphUrlUseCase
import com.metasearch.android.domain.graph.api.usecase.GetGraphImageUriUseCase
import com.metasearch.android.feature.detail.R
import com.metasearch.android.feature.screens.GraphDetailScreen
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
class GraphDetailPresenter(
    @Assisted private val screen: GraphDetailScreen,
    @Assisted private val navigator: Navigator,
    private val getDetailGraphUrlUseCase: GetDetailGraphUrlUseCase,
    private val getGraphImageUriUseCase: GetGraphImageUriUseCase,
) : Presenter<GraphDetailUiState> {

    @CircuitInject(GraphDetailScreen::class, AppScope::class)
    @AssistedFactory
    interface Factory {
        fun create(
            screen: GraphDetailScreen,
            navigator: Navigator,
        ): GraphDetailPresenter
    }

    @Composable
    override fun present(): GraphDetailUiState {
        val scope = rememberCoroutineScope()
        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Loading) }
        var sideEffect by rememberRetained { mutableStateOf<GraphDetailSideEffect?>(null) }
        var webViewUrl by remember { mutableStateOf("") }
        var selectedImages by remember { mutableStateOf<ImmutableList<String>>(persistentListOf()) }
        val maxImages = 10

        LaunchedEffect(Unit) {
            webViewUrl = getDetailGraphUrlUseCase(screen.entityName)
        }

        fun handleEvent(event: GraphDetailUiEvent) {
            when (event) {
                GraphDetailUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                GraphDetailUiEvent.OnWebLoading -> {
                    uiState = UiState.Loading
                }

                GraphDetailUiEvent.OnWebSuccess -> {
                    uiState = UiState.Success
                }

                is GraphDetailUiEvent.OnWebError -> {
                    uiState = UiState.Error(event.message)
                }

                GraphDetailUiEvent.OnRetry -> {
                    scope.launch {
                        uiState = UiState.Loading
                        webViewUrl = ""
                        webViewUrl = getDetailGraphUrlUseCase(screen.entityName)
                    }
                }

                is GraphDetailUiEvent.OnPhotoSelected -> {
                    scope.launch {
                        val uriString = getGraphImageUriUseCase(event.photoName)
                        if (uriString != null) {
                            if (!selectedImages.contains(uriString)) {
                                selectedImages = (listOf(uriString) + selectedImages).take(maxImages).toPersistentList()
                            }
                        } else {
                            sideEffect = GraphDetailSideEffect.ShowToast(UiText.StringResource(R.string.graph_detail_screen_error))
                        }
                    }
                }

                GraphDetailUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is GraphDetailUiEvent.OnImageClick -> {
                    navigator.goTo(PhotoDetailScreen(event.uriString))
                }
            }
        }

        return GraphDetailUiState(
            uiState = uiState,
            webViewUrl = webViewUrl,
            selectedImages = selectedImages,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
