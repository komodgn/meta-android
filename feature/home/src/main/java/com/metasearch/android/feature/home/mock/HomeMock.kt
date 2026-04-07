package com.metasearch.android.feature.home.mock

import androidx.compose.ui.geometry.Offset
import androidx.paging.PagingData
import com.metasearch.android.data.domain.GalleryImage
import com.metasearch.android.data.domain.Person
import com.metasearch.android.data.domain.fakes
import com.metasearch.android.feature.home.HomeUiState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.flowOf

fun HomeUiState.Companion.mock(): HomeUiState = HomeUiState(
    isAnalyzing = false,
    isExpanded = true,
    persons = Person.fakes().toPersistentList(),
    images = flowOf(PagingData.from(GalleryImage.fakes())),
    selectedLongClickImage = null,
    selectedOffset = Offset.Zero,
    eventSink = {},
)
