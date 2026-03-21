package com.metasearch.android.feature.home.mock

import androidx.compose.ui.geometry.Offset
import androidx.paging.PagingData
import com.metasearch.android.core.model.GalleryImage
import com.metasearch.android.core.model.Person
import com.metasearch.android.core.model.fakes
import com.metasearch.android.feature.home.HomeUiState
import kotlinx.coroutines.flow.flowOf

fun HomeUiState.Companion.mock(): HomeUiState = HomeUiState(
    isAnalyzing = false,
    isExpanded = true,
    persons = Person.fakes(),
    images = flowOf(PagingData.from(GalleryImage.fakes())),
    selectedLongClickImage = null,
    selectedOffset = Offset.Zero,
    eventSink = {},
)
