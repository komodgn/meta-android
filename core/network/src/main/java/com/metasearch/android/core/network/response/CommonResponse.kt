package com.metasearch.android.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse(
    val message: String? = null,
    val error: String? = null,
)
