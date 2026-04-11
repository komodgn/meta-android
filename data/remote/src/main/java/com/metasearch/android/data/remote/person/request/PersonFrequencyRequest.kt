package com.metasearch.android.data.remote.person.request

import kotlinx.serialization.Serializable

@Serializable
data class PersonFrequencyRequest(
    val dbName: String,
    val personNames: List<String>,
)
