package com.metasearch.android.feature.detail.photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.common.utils.handleException
import com.metasearch.android.domain.analysis.api.usecase.GetImageDescriptionUseCase
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import com.metasearch.android.feature.detail.R
import com.metasearch.android.feature.screens.FocusingSearchScreen
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
import kotlinx.coroutines.launch

@AssistedInject
class PhotoDetailPresenter(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: PhotoDetailScreen,
    private val getImageDescriptionUseCase: GetImageDescriptionUseCase,
    private val galleryRepository: GalleryRepository,
) : Presenter<PhotoDetailUiState> {

    @CircuitInject(PhotoDetailScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: PhotoDetailScreen,
            navigator: Navigator,
        ): PhotoDetailPresenter
    }

    @Composable
    override fun present(): PhotoDetailUiState {
        val scope = rememberCoroutineScope()
        var isLoading by remember { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<PhotoDetailSideEffect?>(null) }
        val imageUriString by rememberRetained { mutableStateOf(screen.imageUriString) }
        var imageDescription by remember { mutableStateOf<String?>(null) }

        fun handleEvent(event: PhotoDetailUiEvent) {
            when (event) {
                PhotoDetailUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is PhotoDetailUiEvent.OnCreateImageDescriptionClick -> {
                    isLoading = true

                    scope.launch {
                        getImageDescriptionUseCase(event.imageUriString)
                            .onSuccess { description ->
                                imageDescription = description
                            }
                            .onFailure { exception ->
                                handleException(exception, onError = { sideEffect = PhotoDetailSideEffect.ShowToast(it) })
                            }

                        isLoading = false
                    }
                }

                is PhotoDetailUiEvent.OnGraphClick -> {
                    scope.launch {
                        val fileName = galleryRepository.getFileName(screen.imageUriString)
                        if (fileName.isNullOrBlank()) {
                            sideEffect = PhotoDetailSideEffect.ShowToast(
                                UiText.StringResource(R.string.photo_detail_screen_graph_load_failed),
                            )
                            return@launch
                        }

                        navigator.goTo(GraphDetailScreen(entityName = fileName))
                    }
                }

                is PhotoDetailUiEvent.OnFocusingSearchClick -> {
                    navigator.goTo(
                        FocusingSearchScreen(
                            imageUriString = event.imageUriString,
                        ),
                    )
                }

                is PhotoDetailUiEvent.OnShareImageClick -> {
                    sideEffect = PhotoDetailSideEffect.ShareImage(
                        uriString = event.imageUriString,
                    )
                }

                PhotoDetailUiEvent.OnBackClick -> {
                    navigator.pop()
                }
            }
        }

        return PhotoDetailUiState(
            isLoading = isLoading,
            imageUriString = imageUriString,
            imageDescription = imageDescription,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
