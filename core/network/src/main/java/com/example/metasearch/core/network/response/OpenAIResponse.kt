package com.example.metasearch.core.network.response

import com.example.metasearch.core.network.request.OpenAIMessage
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponse(
    val choices: List<OpenAIChoice>,
)

@Serializable
data class OpenAIChoice(
    val message: OpenAIMessage,
)
