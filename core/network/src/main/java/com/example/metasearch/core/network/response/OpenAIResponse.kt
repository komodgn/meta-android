package com.example.metasearch.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponse(
    val id: String,
    val created: Long,
    val model: String,
)
