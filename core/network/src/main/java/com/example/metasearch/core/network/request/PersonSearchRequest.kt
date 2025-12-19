package com.example.metasearch.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class PersonSearchRequest(
    val dbName: String,
    val personName: String,
)
