package com.metasearch.android.data.remote.analysis.response

import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse(
    val message: String? = null,
    val error: String? = null,
)
