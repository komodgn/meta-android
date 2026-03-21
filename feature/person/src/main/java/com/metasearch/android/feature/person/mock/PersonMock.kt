package com.metasearch.android.feature.person.mock

import com.metasearch.android.core.model.Person
import com.metasearch.android.core.model.fakes
import com.metasearch.android.feature.person.PersonUiState

fun PersonUiState.Companion.mock() = PersonUiState(
    inputPersonNameString = "라이언",
    people = Person.fakes(),
    eventSink = {},
)
