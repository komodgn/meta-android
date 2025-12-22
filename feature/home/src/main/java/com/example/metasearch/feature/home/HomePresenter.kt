package com.example.metasearch.feature.home

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.metasearch.core.data.api.repository.GalleryRepository
import com.example.metasearch.core.data.api.repository.ImageAnalysisRepository
import com.example.metasearch.core.data.api.repository.PersonRepository
import com.example.metasearch.core.model.PersonModel
import com.example.metasearch.feature.home.worker.ImageAnalysisWorker
import com.example.metasearch.feature.screens.HomeScreen
import com.example.metasearch.feature.screens.PersonDetailScreen
import com.example.metasearch.feature.screens.PhotoDetailScreen
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

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        var isLoading by remember { mutableStateOf(false) }
        val isAnalyzing by remember(context) {
            imageAnalysisRepository.getAnalysisStatus(context)
        }.collectAsState(initial = false)
        var isExpanded by remember { mutableStateOf(false) }

        val localPersons by personRepository.getHomeDisplayPersons().collectAsState(initial = emptyList())
        var displayPersons by remember { mutableStateOf<List<PersonModel>>(emptyList()) }

        var images by remember { mutableStateOf<List<Uri>>(emptyList()) }

        LaunchedEffect(localPersons) {
            displayPersons = localPersons
        }

        LaunchedEffect(Unit) {
            images = galleryRepository.getAllGalleryImages()
        }

        fun handleEvent(event: HomeUiEvent) {
            when (event) {
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
                        workRequest
                    )
                }

                HomeUiEvent.OnPersonSectionExpand -> {
                    isExpanded = !isExpanded

                    if (isExpanded) {
                        scope.launch {
                            isLoading = true
                            displayPersons = personRepository.fetchAndSyncPhotoCount(displayPersons)
                            isLoading = false
                        }
                    }
                }

                is HomeUiEvent.OnPersonClick -> navigator.goTo(
                    PersonDetailScreen(
                        event.personId,
                    ),
                )

                is HomeUiEvent.OnImageClick -> navigator.goTo(
                    PhotoDetailScreen(
                        event.imageUriString,
                    ),
                )

                is HomeUiEvent.OnTabClick -> navigator.resetRoot(event.screen)
            }
        }

        return HomeUiState(
            isLoading = isLoading,
            isAnalyzing = isAnalyzing,
            isExpanded = isExpanded,
            persons = displayPersons,
            images = images,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
