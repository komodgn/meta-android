package com.example.metasearch.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class PersonFrequencyResponse(
    val name: String,
    val entityType: String,
    val frequency: Int,
)
