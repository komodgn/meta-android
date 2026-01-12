package com.metasearch.android.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class TripleResponse(
    val triple: String,
)
