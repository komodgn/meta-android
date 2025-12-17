package com.example.metasearch.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIRequest(
    val model: String,
    val prompt: String,
    val temperature: Double,
    val maxTokens: Int,
)
