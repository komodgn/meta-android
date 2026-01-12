package com.metasearch.android.core.network.service

import com.metasearch.android.core.network.request.OpenAIRequest
import com.metasearch.android.core.network.response.OpenAIResponse
import retrofit2.http.Body
import retrofit2.http.POST

public interface OpenAIService {
    @POST("/v1/chat/completions")
    suspend fun createChatCompletion(
        @Body request: OpenAIRequest,
    ): OpenAIResponse
}
