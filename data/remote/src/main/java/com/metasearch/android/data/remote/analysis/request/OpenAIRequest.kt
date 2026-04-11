package com.metasearch.android.data.remote.analysis.request

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIRequest(
    val model: String,
    val messages: List<OpenAIMessage>,
)

@Serializable
data class OpenAIMessage(
    val role: String,
    val content: String,
)
