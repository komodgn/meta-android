package com.metasearch.android.feature.home.mock

import androidx.compose.ui.geometry.Offset
import androidx.paging.PagingData
import com.metasearch.android.core.model.FaceModel
import com.metasearch.android.core.model.GalleryImageModel
import com.metasearch.android.core.model.PersonModel
import com.metasearch.android.feature.home.HomeUiState
import kotlinx.coroutines.flow.flowOf

internal val fakeHomeImages = List(30) { index ->
    GalleryImageModel(
        id = index.toLong(),
        uriString = "https://picsum.photos/seed/${index + 100}/200/200",
        dateAdded = System.currentTimeMillis(),
    )
}

internal val fakePeople = listOf(
    PersonModel(
        id = 1L,
        name = "인물1",
        inputName = "춘식이",
        faces = listOf(
            FaceModel(
                id = 101L,
                personId = 1L,
                imageName = "face1.jpg",
                imageData = byteArrayOf(),
            ),
        ),
        isHomeDisplay = true,
    ),
    PersonModel(
        id = 2L,
        name = "인물2",
        inputName = "라이언",
        faces = listOf(
            FaceModel(
                id = 102L,
                personId = 2L,
                imageName = "face2.jpg",
                imageData = byteArrayOf(),
            ),
        ),
        isHomeDisplay = true,
    ),
    PersonModel(
        id = 3L,
        name = "인물3",
        inputName = "무지",
        faces = listOf(
            FaceModel(
                id = 103L,
                personId = 3L,
                imageName = "face3.jpg",
                imageData = byteArrayOf(),
            ),
        ),
        isHomeDisplay = true,
    ),
)

internal val homeUiStateMock = HomeUiState(
    isAnalyzing = false,
    isExpanded = true,
    persons = fakePeople,
    images = flowOf(PagingData.from(fakeHomeImages)),
    selectedLongClickImage = null,
    selectedOffset = Offset.Zero,
    eventSink = {},
)
