package com.example.metasearch.feature.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.metasearch.core.data.api.repository.GraphRepository
import com.example.metasearch.feature.screens.GraphDetailScreen
import com.example.metasearch.feature.screens.GraphScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
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
        var webViewUrl by remember { mutableStateOf("") }
        var selectedImages by remember { mutableStateOf(listOf<String>()) }
        var errorMessage by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()
        val imageNotFoundMessage = stringResource(R.string.graph_screen_image_not_found_error)
        val maxImages = 10

        LaunchedEffect(Unit) {
//            webViewUrl = "https://www.google.com"
            webViewUrl = graphRepository.getFullGraphWebViewUrl()
        }

        fun handleEvent(event: GraphUiEvent) {
            when (event) {
                is GraphUiEvent.OnPhotoSelected -> {
                    coroutineScope.launch {
                        val uri = graphRepository.findMatchedUri(event.photoName)
                        if (uri != null) {
                            val uriString = uri.toString()
                            if (!selectedImages.contains(uriString)) {
                                selectedImages = (listOf(uriString) + selectedImages).take(maxImages)
                            }
                        } else {
                            errorMessage = imageNotFoundMessage
                        }
                    }
                }

                is GraphUiEvent.OnImageClick -> {
                    navigator.goTo(GraphDetailScreen(event.uriString))
                }

                GraphUiEvent.OnErrorDialogDismiss -> errorMessage = ""

                is GraphUiEvent.OnTabClick -> navigator.resetRoot(event.screen)
            }
        }

        return GraphUiState(
            webViewUrl = webViewUrl,
            selectedImages = selectedImages,
            errorMessage = errorMessage,
            eventSink = ::handleEvent,
        )
    }
}
