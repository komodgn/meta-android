package com.metasearch.android.data.remote.person.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteEntityRequest(
    val dbName: String,
    val entityName: String,
)
