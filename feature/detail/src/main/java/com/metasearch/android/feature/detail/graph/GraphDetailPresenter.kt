package com.metasearch.android.feature.detail.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.data.api.repository.GraphRepository
import com.metasearch.android.feature.detail.R
import com.metasearch.android.feature.screens.GraphDetailScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class GraphDetailPresenter @AssistedInject constructor(
    @Assisted private val screen: GraphDetailScreen,
    @Assisted private val navigator: Navigator,
    private val graphRepository: GraphRepository,
) : Presenter<GraphDetailUiState> {

    @CircuitInject(GraphDetailScreen::class, ActivityRetainedComponent::class)
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
        var sideEffect by remember { mutableStateOf<GraphDetailSideEffect?>(null) }
        var webViewUrl by remember { mutableStateOf("") }
        var selectedImages by remember { mutableStateOf<ImmutableList<String>>(persistentListOf()) }
        val maxImages = 10

        LaunchedEffect(Unit) {
            webViewUrl = graphRepository.getDetailGraphWebViewUrl(screen.entityName)
        }

        fun handleEvent(event: GraphDetailUiEvent) {
            when (event) {
                GraphDetailUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is GraphDetailUiEvent.OnPhotoSelected -> {
                    scope.launch {
                        val uri = graphRepository.findMatchedUri(event.photoName)
                        if (uri != null) {
                            val uriString = uri.toString()
                            if (!selectedImages.contains(uriString)) {
                                selectedImages = (listOf(uriString) + selectedImages).take(maxImages).toPersistentList()
                            }
                        } else {
                            sideEffect = GraphDetailSideEffect.ShowToast(
                                message = UiText.StringResource(R.string.graph_detail_screen_error),
                            )
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
            webViewUrl = webViewUrl,
            selectedImages = selectedImages,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
