package com.metasearch.android.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class PersonFrequencyRequest(
    val dbName: String,
    val personNames: List<String>,
)
