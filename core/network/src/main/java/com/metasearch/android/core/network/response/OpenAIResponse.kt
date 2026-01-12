package com.metasearch.android.core.network.response

import com.metasearch.android.core.network.request.OpenAIMessage
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponse(
    val choices: List<OpenAIChoice>,
)

@Serializable
data class OpenAIChoice(
    val message: OpenAIMessage,
)
