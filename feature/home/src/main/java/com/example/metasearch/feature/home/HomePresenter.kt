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
import com.example.metasearch.core.data.api.repository.GalleryRepository
import com.example.metasearch.core.data.api.repository.PersonRepository
import com.example.metasearch.core.model.PersonModel
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
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()
        var isLoading by remember { mutableStateOf(false) }
        var isAnalyzing by remember { mutableStateOf(false) }
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

                HomeUiEvent.OnStartAnalysisClicked -> {
//                    val constraints = Constraints.Builder()
//                        .setRequiredNetworkType(NetworkType.CONNECTED)
//                        .build()

//                    val analysisWorkRequest = OneTimeWorkRequestBuilder<ImageAnalysisWorker>()
//                        .setConstraints(constraints)
//                        .build()
//
//                    WorkManager.getInstance(context).enqueueUniqueWork(
//                        "ImageAnalysisWork",
//                        ExistingWorkPolicy.KEEP,
//                        analysisWorkRequest
//                    )
                }

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
