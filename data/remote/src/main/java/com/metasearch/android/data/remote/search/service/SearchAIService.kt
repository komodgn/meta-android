package com.metasearch.android.data.remote.search.service

import com.metasearch.android.data.remote.search.request.FocusingSearchRequest
import com.metasearch.android.data.remote.search.response.CircleDetectionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SearchAIService {
    @Multipart
    @POST("android/circle_search")
    suspend fun uploadImageAndCircles(
        @Part image: MultipartBody.Part,
        @Part("dbName") dbName: RequestBody,
        @Part("circles") request: FocusingSearchRequest,
    ): CircleDetectionResponse
}
