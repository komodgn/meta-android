package com.metasearch.android.data.remote.person.request

import kotlinx.serialization.Serializable

@Serializable
data class ChangeNameRequest(
    val dbName: String,
    val oldName: String,
    val newName: String,
)
