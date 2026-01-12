package com.metasearch.android.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class PersonSearchRequest(
    val dbName: String,
    val personName: String,
)
