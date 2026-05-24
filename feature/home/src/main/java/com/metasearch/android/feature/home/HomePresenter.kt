package com.metasearch.android.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.paging.cachedIn
import androidx.work.WorkInfo
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.worker.api.constants.ModelDownloadKeys
import com.metasearch.android.core.worker.api.constants.WorkerNames
import com.metasearch.android.core.worker.api.usecase.WorkScheduleUseCase
import com.metasearch.android.core.worker.api.usecase.WorkerStatusUseCase
import com.metasearch.android.data.domain.Person
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import com.metasearch.android.domain.person.api.usecase.GetHomeDisplayPersonsUseCase
import com.metasearch.android.domain.search.api.repository.ModelRepository
import com.metasearch.android.domain.search.api.repository.SearchRepository
import com.metasearch.android.domain.search.api.usecase.StartModelDownloadUseCase
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

@Suppress("LongParameterList")
@AssistedInject
class HomePresenter(
    @Assisted private val navigator: Navigator,
    private val galleryRepository: GalleryRepository,
    private val getHomeDisplayPersonsUseCase: GetHomeDisplayPersonsUseCase,
    private val workScheduleUseCase: WorkScheduleUseCase,
    private val workerStatusUseCase: WorkerStatusUseCase,
    private val searchRepository: SearchRepository,
    private val startModelDownloadUseCase: StartModelDownloadUseCase,
    private val modelRepository: ModelRepository,
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
            .monitoringUniqueJobStatus(WorkerNames.IMAGE_ANALYSIS_WORK)
            .collectAsState(initial = null)

        val downloadWorkInfo by workerStatusUseCase
            .monitorUniqueJob(WorkerNames.GLOBAL_MODEL_DOWNLOAD)
            .collectAsState(initial = null)

        val downloadingModelId by remember(downloadWorkInfo) {
            derivedStateOf {
                if (downloadWorkInfo?.state == WorkInfo.State.RUNNING) {
                    downloadWorkInfo?.progress?.getString(ModelDownloadKeys.KEY_MODEL_NAME)
                } else {
                    null
                }
            }
        }

        var installedModelIds by rememberRetained { mutableStateOf(emptySet<String>()) }

        var isExpanded by rememberRetained { mutableStateOf(false) }

        val localPersons by getHomeDisplayPersonsUseCase()
            .collectAsState(initial = emptyList())
        var displayPersons by rememberRetained { mutableStateOf(persistentListOf<Person>()) }

        val galleryPagingFlow = rememberRetained {
            galleryRepository.getGalleryPagingData().cachedIn(scope)
        }
        var selectedLongClickImage by remember { mutableStateOf<String?>(null) }
        var selectedOffset by remember { mutableStateOf(Offset.Zero) }

        fun refreshInstalledModelIds() {
            installedModelIds = modelRepository.getAllModels()
                .filter { model -> searchRepository.isLocalModelAvailable(model) }
                .map { it.modelId }
                .toSet()
        }

        LaunchedEffect(downloadWorkInfo?.state) {
            refreshInstalledModelIds()
        }

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

                is HomeUiEvent.OnDownloadModelClick -> {
                    if (downloadWorkInfo?.state == WorkInfo.State.RUNNING) {
                        sideEffect = HomeSideEffect.ShowToast(UiText.StringResource(R.string.home_screen_ai_download_alert))
                    } else {
                        startModelDownloadUseCase.invoke(event.model)
                    }
                }

                is HomeUiEvent.OnDeleteModelClick -> {
                    scope.launch {
                        modelRepository.deleteModel(
                            event.model.normalizedName,
                            event.model.version,
                        )

                        refreshInstalledModelIds()
                    }
                }

                HomeUiEvent.OnStartAnalysisClicked -> {
                    workScheduleUseCase.scheduleNow(
                        workName = WorkerNames.IMAGE_ANALYSIS_WORK,
                        klass = ImageAnalysisWorker::class,
                    )
                }

                HomeUiEvent.OnPersonSectionExpand -> {
                    isExpanded = !isExpanded

                    if (isExpanded) {
                        scope.launch {
                            isPersonLoading = true
                            val syncedPersons = getHomeDisplayPersonsUseCase.syncAndGet(displayPersons)

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
            availableModels = modelRepository.getAllModels().toPersistentList(),
            installedModelIds = installedModelIds,
            downloadingModelId = downloadingModelId,
            isDownloading = downloadWorkInfo?.state == WorkInfo.State.RUNNING,
            downloadProgress = downloadWorkInfo?.progress?.getFloat("KEY_PROGRESS", 0f) ?: 0f,
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
