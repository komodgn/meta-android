package com.example.metasearch.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class PersonFrequencyRequest(
    val dbName: String,
    val personNames: List<String>,
)
