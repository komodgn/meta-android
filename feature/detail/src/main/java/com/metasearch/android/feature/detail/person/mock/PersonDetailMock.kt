package com.metasearch.android.feature.detail.person.mock

import androidx.core.net.toUri
import com.metasearch.android.core.model.Person
import com.metasearch.android.core.model.fake
import com.metasearch.android.feature.detail.person.PersonDetailUiState
import kotlinx.collections.immutable.toPersistentList

internal val fakePhotoUris = (1..10).map { i ->
    "https://picsum.photos/seed/$i/200/200".toUri()
}.toPersistentList()

fun PersonDetailUiState.Companion.mock(
    isLoading: Boolean = false,
    person: Person? = Person.fake(),
) = PersonDetailUiState(
    isLoading = isLoading,
    person = person,
    photoUris = fakePhotoUris,
    eventSink = {},
)
