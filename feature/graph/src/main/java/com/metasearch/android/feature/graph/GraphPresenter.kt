package com.metasearch.android.feature.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.data.api.repository.GraphRepository
import com.metasearch.android.feature.screens.GraphScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
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

class GraphPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val graphRepository: GraphRepository,
) : Presenter<GraphUiState> {

    @CircuitInject(GraphScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    interface Factory {
        fun create(
            navigator: Navigator,
        ): GraphPresenter
    }

    @Composable
    override fun present(): GraphUiState {
        val coroutineScope = rememberCoroutineScope()
        var sideEffect by remember { mutableStateOf<GraphSideEffect?>(null) }
        var webViewUrl by rememberRetained { mutableStateOf("") }
        var selectedImages by rememberRetained { mutableStateOf<ImmutableList<String>>(persistentListOf()) }
        val maxImages = 10

        LaunchedEffect(Unit) {
            webViewUrl = graphRepository.getFullGraphWebViewUrl()
        }

        fun handleEvent(event: GraphUiEvent) {
            when (event) {
                GraphUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is GraphUiEvent.OnPhotoSelected -> {
                    coroutineScope.launch {
                        val uri = graphRepository.findMatchedUri(event.photoName)
                        if (uri != null) {
                            val uriString = uri.toString()
                            if (!selectedImages.contains(uriString)) {
                                selectedImages = (listOf(uriString) + selectedImages).take(maxImages).toPersistentList()
                            }
                        } else {
                            sideEffect = GraphSideEffect.ShowToast(
                                message = UiText.StringResource(R.string.graph_screen_image_not_found_error),
                            )
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
            webViewUrl = webViewUrl,
            selectedImages = selectedImages,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
