package com.metasearch.android.data.remote.person.service

import com.metasearch.android.data.remote.person.request.ChangeNameRequest
import com.metasearch.android.data.remote.person.request.DeleteEntityRequest
import com.metasearch.android.data.remote.person.request.PersonFrequencyRequest
import com.metasearch.android.data.remote.person.request.PersonSearchRequest
import com.metasearch.android.data.remote.person.response.ChangeNameResponse
import com.metasearch.android.data.remote.person.response.DeleteEntityResponse
import com.metasearch.android.data.remote.person.response.PersonFrequencyResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PersonWebService {
    @POST("neo4j/deleteEntity/")
    suspend fun deleteEntity(
        @Body request: DeleteEntityRequest,
    ): DeleteEntityResponse

    @POST("personsearch")
    suspend fun sendPersonData(
        @Body request: PersonSearchRequest,
    ): List<String>

    @POST("changename")
    suspend fun changePersonName(
        @Body request: ChangeNameRequest,
    ): ChangeNameResponse

    @POST("/api/peoplefrequency")
    suspend fun getPersonFrequency(
        @Body request: PersonFrequencyRequest,
    ): PersonFrequencyResponse
}
