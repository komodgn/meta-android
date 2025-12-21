package com.example.metasearch.feature.detail.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.metasearch.core.data.api.repository.GraphRepository
import com.example.metasearch.feature.detail.R
import com.example.metasearch.feature.screens.GraphDetailScreen
import com.example.metasearch.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class GraphDetailPresenter @AssistedInject constructor(
    @Assisted private val screen: GraphDetailScreen,
    @Assisted private val navigator: Navigator,
    private val graphRepository: GraphRepository,
) : Presenter<GraphDetailUiState> {

    @Composable
    override fun present(): GraphDetailUiState {
        var webViewUrl by remember { mutableStateOf("") }
        var selectedImages by remember { mutableStateOf(listOf<String>()) }
        var errorMessage by remember { mutableStateOf("") }
        val failedToFindImage = stringResource(R.string.graph_detail_screen_error)
        val maxImages = 10

        LaunchedEffect(Unit) {
            webViewUrl = screen.imageUriString
//            webViewUrl = graphRepository.getDetailGraphWebViewUrl(screen.imageUriString)
        }

        fun handleEvent(event: GraphDetailUiEvent) {
            when (event) {
                is GraphDetailUiEvent.OnPhotoSelected -> {
                    val scope = kotlinx.coroutines.MainScope()
                    scope.launch {
                        val uri = graphRepository.findMatchedUri(event.photoName)
                        if (uri != null) {
                            val uriString = uri.toString()
                            if (!selectedImages.contains(uriString)) {
                                selectedImages = (listOf(uriString) + selectedImages).take(maxImages)
                            }
                        } else {
                            errorMessage = failedToFindImage
                        }
                    }
                }

                GraphDetailUiEvent.OnBackClick -> navigator.pop()

                is GraphDetailUiEvent.OnImageClick -> {
                    navigator.goTo(PhotoDetailScreen(event.uriString))
                }

                GraphDetailUiEvent.OnErrorDialogDismiss -> errorMessage = ""
            }
        }

        return GraphDetailUiState(
            webViewUrl = webViewUrl,
            selectedImages = selectedImages,
            errorMessage = errorMessage,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(GraphDetailScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    interface Factory {
        fun create(
            screen: GraphDetailScreen,
            navigator: Navigator,
        ): GraphDetailPresenter
    }
}
