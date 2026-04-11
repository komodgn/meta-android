package com.metasearch.android.data.remote.analysis.response

import com.metasearch.android.data.remote.analysis.request.OpenAIMessage
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponse(
    val choices: List<OpenAIChoice>,
)

@Serializable
data class OpenAIChoice(
    val message: OpenAIMessage,
)
