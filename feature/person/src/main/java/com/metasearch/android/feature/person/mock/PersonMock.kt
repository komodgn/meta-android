package com.metasearch.android.feature.person.mock

import com.metasearch.android.data.domain.Person
import com.metasearch.android.data.domain.fakes
import com.metasearch.android.feature.person.PersonUiState
import kotlinx.collections.immutable.toPersistentList

fun PersonUiState.Companion.mock() = PersonUiState(
    inputPersonNameString = "라이언",
    people = Person.fakes().toPersistentList(),
    eventSink = {},
)
