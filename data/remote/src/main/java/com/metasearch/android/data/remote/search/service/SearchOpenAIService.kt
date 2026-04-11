package com.metasearch.android.data.remote.search.service

import com.metasearch.android.data.remote.analysis.request.OpenAIRequest
import com.metasearch.android.data.remote.analysis.response.OpenAIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchOpenAIService {
    @POST("/v1/chat/completions")
    suspend fun createChatCompletion(
        @Body request: OpenAIRequest,
    ): OpenAIResponse
}
