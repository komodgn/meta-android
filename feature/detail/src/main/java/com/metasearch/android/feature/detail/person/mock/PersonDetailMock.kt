package com.metasearch.android.feature.detail.person.mock

import androidx.core.net.toUri
import com.metasearch.android.core.model.FaceModel
import com.metasearch.android.core.model.PersonModel
import com.metasearch.android.feature.detail.person.PersonDetailUiState
import kotlinx.collections.immutable.toPersistentList

internal val fakeFaces = listOf(
    FaceModel(
        id = 1L,
        personId = 1L,
        imageName = "인물1",
        imageData = ByteArray(
            size = 3,
        ),
    ),
    FaceModel(
        id = 2L,
        personId = 1L,
        imageName = "인물1",
        imageData = ByteArray(
            size = 2,
        ),
    ),
)

internal val fakePerson = PersonModel(
    id = 1L,
    name = "인물1",
    inputName = "라이언",
    faces = fakeFaces,
)

internal val fakePhotoUris = listOf(
    "https://picsum.photos/seed/1/200/200".toUri(),
    "https://picsum.photos/seed/2/200/200".toUri(),
    "https://picsum.photos/seed/3/200/200".toUri(),
    "https://picsum.photos/seed/4/200/200".toUri(),
    "https://picsum.photos/seed/5/200/200".toUri(),
    "https://picsum.photos/seed/6/200/200".toUri(),
    "https://picsum.photos/seed/7/200/200".toUri(),
    "https://picsum.photos/seed/8/200/200".toUri(),
    "https://picsum.photos/seed/9/200/200".toUri(),
    "https://picsum.photos/seed/10/200/200".toUri(),
)

internal val personDetailUiStateMock = PersonDetailUiState(
    isLoading = false,
    person = fakePerson,
    photoUris = fakePhotoUris.toPersistentList(),
    showPhotoSelectDialog = false,
    eventSink = {},
)
