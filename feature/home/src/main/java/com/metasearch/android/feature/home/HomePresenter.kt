package com.metasearch.android.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.paging.cachedIn
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.metasearch.android.core.data.api.repository.GalleryRepository
import com.metasearch.android.core.data.api.repository.ImageAnalysisRepository
import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.core.model.PersonModel
import com.metasearch.android.feature.home.worker.ImageAnalysisWorker
import com.metasearch.android.feature.screens.HomeScreen
import com.metasearch.android.feature.screens.PersonDetailScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val galleryRepository: GalleryRepository,
    private val personRepository: PersonRepository,
    private val imageAnalysisRepository: ImageAnalysisRepository,
) : Presenter<HomeUiState> {

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        var sideEffect by remember { mutableStateOf<HomeSideEffect?>(null) }
        var isPersonLoading by remember { mutableStateOf(false) }
        val isAnalyzing by remember(context) {
            imageAnalysisRepository.getAnalysisStatus(context)
        }.collectAsState(initial = false)
        var isExpanded by remember { mutableStateOf(false) }

        val localPersons by personRepository.getHomeDisplayPersons().collectAsState(initial = emptyList())
        var displayPersons by remember { mutableStateOf<List<PersonModel>>(emptyList()) }

        val galleryPagingFlow = remember {
            galleryRepository.getGalleryPagingData().cachedIn(scope)
        }
        var selectedLongClickImage by remember { mutableStateOf<String?>(null) }
        var selectedOffset by remember { mutableStateOf(Offset.Zero) }

        LaunchedEffect(localPersons) {
            displayPersons = localPersons
        }

        fun handleEvent(event: HomeUiEvent) {
            when (event) {
                HomeUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                HomeUiEvent.OnStartAnalysisClicked -> {
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                    val workRequest = OneTimeWorkRequestBuilder<ImageAnalysisWorker>()
                        .setConstraints(constraints)
                        .build()

                    WorkManager.getInstance(context).enqueueUniqueWork(
                        "ImageAnalysisWork",
                        ExistingWorkPolicy.KEEP,
                        workRequest,
                    )
                }

                HomeUiEvent.OnPersonSectionExpand -> {
                    isExpanded = !isExpanded

                    if (isExpanded) {
                        scope.launch {
                            isPersonLoading = true
                            displayPersons = personRepository.fetchAndSyncPhotoCount(displayPersons)
                            isPersonLoading = false
                        }
                    }
                }

                is HomeUiEvent.OnPersonClick -> {
                    navigator.goTo(
                        PersonDetailScreen(
                            event.personId,
                        ),
                    )
                }

                is HomeUiEvent.OnImageClick -> {
                    navigator.goTo(
                        PhotoDetailScreen(
                            event.imageUriString,
                        ),
                    )
                }

                is HomeUiEvent.OnImageLongClick -> {
                    selectedLongClickImage = event.imageUriString
                    selectedOffset = event.offSet
                }

                HomeUiEvent.OnLongClickCancel -> {
                    selectedLongClickImage = null
                }

                is HomeUiEvent.OnShareRelease -> {
                    sideEffect = HomeSideEffect.ShareImage(event.imageUriString)
                    selectedLongClickImage = null
                }

                is HomeUiEvent.OnTabClick -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return HomeUiState(
            isPersonLoading = isPersonLoading,
            isAnalyzing = isAnalyzing,
            isExpanded = isExpanded,
            persons = displayPersons,
            images = galleryPagingFlow,
            selectedLongClickImage = selectedLongClickImage,
            selectedOffset = selectedOffset,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
