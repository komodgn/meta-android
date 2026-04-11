package com.metasearch.android.data.remote.person.service

import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PersonAIService {
    @Multipart
    @POST("android/delete_person")
    suspend fun deletePerson(
        @Part("dbName") dbName: RequestBody,
        @Part("deletePerson") deletePerson: RequestBody,
    )
}
