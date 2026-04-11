package com.metasearch.android.data.remote.person.request

import kotlinx.serialization.Serializable

@Serializable
data class PersonSearchRequest(
    val dbName: String,
    val personName: String,
)
