package com.metasearch.android.feature.home

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.paging.PagingData
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.data.domain.GalleryImage
import com.metasearch.android.data.domain.Model
import com.metasearch.android.data.domain.Person
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow

data class HomeUiState(
    val isModelAvailable: Boolean = false,
    val downloadingModelId: String? = null,
    val installedModelIds: Set<String> = setOf(),
    val availableModels: ImmutableList<Model> = persistentListOf(),
    val isDownloading: Boolean = false,
    val downloadProgress: Float = 0f,
    val isPersonLoading: Boolean = false,
    val isAnalyzing: Boolean = false,
    val isExpanded: Boolean = false,
    val persons: ImmutableList<Person> = persistentListOf(),
    val images: Flow<PagingData<GalleryImage>>,
    val selectedLongClickImage: String? = null,
    val selectedOffset: Offset = Offset.Zero,
    val sideEffect: HomeSideEffect? = null,
    val eventSink: (HomeUiEvent) -> Unit,
) : CircuitUiState {
    companion object
}

@Immutable
sealed interface HomeSideEffect {
    data class ShowToast(
        val message: UiText,
    ) : HomeSideEffect

    data class ShareImage(
        val uriString: String,
    ) : HomeSideEffect
}

sealed interface HomeUiEvent : CircuitUiEvent {
    data object InitSideEffect : HomeUiEvent

    data class OnDownloadModelClick(
        val model: Model,
    ) : HomeUiEvent

    data object OnStartAnalysisClicked : HomeUiEvent

    data object OnPersonSectionExpand : HomeUiEvent

    data class OnPersonClick(
        val personId: Long,
    ) : HomeUiEvent

    data class OnImageClick(
        val imageUriString: String,
    ) : HomeUiEvent

    data class OnImageLongClick(
        val imageUriString: String,
        val offSet: Offset = Offset.Zero,
    ) : HomeUiEvent

    data object OnLongClickCancel : HomeUiEvent

    data class OnShareRelease(
        val imageUriString: String,
    ) : HomeUiEvent

    data class OnTabClick(
        val screen: Screen,
    ) : HomeUiEvent
}
