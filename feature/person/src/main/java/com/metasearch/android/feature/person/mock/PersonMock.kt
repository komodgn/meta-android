package com.metasearch.android.feature.person.mock

import com.metasearch.android.core.model.PersonModel
import com.metasearch.android.feature.person.PersonUiState

internal val fakePerson = PersonModel(
    id = 1L,
    name = "인물1",
    inputName = "춘식이",
    isHomeDisplay = true,
)

internal val fakePeople = listOf(
    PersonModel(
        id = 1L,
        name = "인물1",
        inputName = "춘식이",
        isHomeDisplay = true,
    ),
    PersonModel(
        id = 2L,
        name = "인물2",
        inputName = "무지",
        isHomeDisplay = true,
    ),
    PersonModel(
        id = 3L,
        name = "인물3",
        inputName = "어피치",
        isHomeDisplay = true,
    ),
    PersonModel(
        id = 4L,
        name = "인물4",
        inputName = "라이언",
        isHomeDisplay = true,
    ),
)

internal val personUiStateMock = PersonUiState(
    showDeleteDialog = false,
    inputPersonNameString = "",
    people = fakePeople,
    eventSink = {},
)
