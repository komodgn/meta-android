package com.example.metasearch.core.network.service

import com.example.metasearch.core.network.request.OpenAIRequest
import com.example.metasearch.core.network.response.OpenAIResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

public interface OpenAIService {
    @Headers("Content-Type: application/json")
    @POST("/v1/chat/completions")
    fun createChatCompletion(
        @Header("Authorization") authToken: String?,
        @Body body: OpenAIRequest?,
    ): retrofit2.Call<OpenAIResponse?>?
}
