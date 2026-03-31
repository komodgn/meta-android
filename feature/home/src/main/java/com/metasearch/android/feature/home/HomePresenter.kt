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
import androidx.paging.cachedIn
import androidx.work.WorkInfo
import com.metasearch.android.api.usecase.WorkScheduleUseCase
import com.metasearch.android.api.usecase.WorkerStatusUseCase
import com.metasearch.android.core.data.api.repository.GalleryRepository
import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.core.model.Person
import com.metasearch.android.feature.home.worker.ImageAnalysisWorker
import com.metasearch.android.feature.screens.HomeScreen
import com.metasearch.android.feature.screens.PersonDetailScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@AssistedInject
class HomePresenter(
    @Assisted private val navigator: Navigator,
    private val galleryRepository: GalleryRepository,
    private val personRepository: PersonRepository,
    private val workScheduleUseCase: WorkScheduleUseCase,
    private val workerStatusUseCase: WorkerStatusUseCase,
) : Presenter<HomeUiState> {

    @CircuitInject(HomeScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()

        var sideEffect by rememberRetained { mutableStateOf<HomeSideEffect?>(null) }
        var isPersonLoading by rememberRetained { mutableStateOf(false) }
        val analisysStatus by workerStatusUseCase
            .monitoringUniqueJobStatus("ImageAnalysisWork")
            .collectAsState(initial = null)
        var isExpanded by rememberRetained { mutableStateOf(false) }

        val localPersons by personRepository.getHomeDisplayPersons().collectAsState(initial = emptyList())
        var displayPersons by rememberRetained { mutableStateOf(persistentListOf<Person>()) }

        val galleryPagingFlow = rememberRetained {
            galleryRepository.getGalleryPagingData().cachedIn(scope)
        }
        var selectedLongClickImage by remember { mutableStateOf<String?>(null) }
        var selectedOffset by remember { mutableStateOf(Offset.Zero) }

        LaunchedEffect(localPersons) {
            if (!isPersonLoading) {
                displayPersons = localPersons.toPersistentList()
            }
        }

        fun handleEvent(event: HomeUiEvent) {
            when (event) {
                HomeUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                HomeUiEvent.OnStartAnalysisClicked -> {
                    workScheduleUseCase.scheduleNow(
                        workName = "ImageAnalysisWork",
                        klass = ImageAnalysisWorker::class,
                    )
                }

                HomeUiEvent.OnPersonSectionExpand -> {
                    isExpanded = !isExpanded

                    if (isExpanded) {
                        scope.launch {
                            isPersonLoading = true
                            val syncedPersons = personRepository.fetchAndSyncPhotoCount(displayPersons)
                            displayPersons = syncedPersons.toPersistentList()
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
            isAnalyzing = analisysStatus == WorkInfo.State.RUNNING,
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
