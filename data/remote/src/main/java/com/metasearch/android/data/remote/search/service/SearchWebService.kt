package com.metasearch.android.data.remote.search.service

import com.metasearch.android.data.remote.search.request.DetectedObjectsRequest
import com.metasearch.android.data.remote.search.request.NLQueryRequest
import com.metasearch.android.data.remote.search.response.PhotoNameResponse
import com.metasearch.android.data.remote.search.response.PhotoResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchWebService {
    @POST("android/circleToSearch")
    suspend fun sendDetectedObjects(
        @Body request: DetectedObjectsRequest,
    ): PhotoResponse

    @POST("/nlqsearch")
    suspend fun sendCypherQuery(
        @Body request: NLQueryRequest,
    ): PhotoNameResponse
}
