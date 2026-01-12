package com.metasearch.android.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonFrequencyResponse(
    @SerialName("frequencies")
    val frequencies: List<Frequency>,
)

@Serializable
data class Frequency(
    @SerialName("personName")
    val personName: String,
    @SerialName("frequency")
    val frequency: Int,
)
