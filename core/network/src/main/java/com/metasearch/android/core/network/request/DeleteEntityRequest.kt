package com.metasearch.android.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteEntityRequest(
    val dbName: String,
    val entityName: String,
)
