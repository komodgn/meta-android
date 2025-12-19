package com.example.metasearch.core.network.service

import com.example.metasearch.core.network.request.ChangeNameRequest
import com.example.metasearch.core.network.request.DeleteEntityRequest
import com.example.metasearch.core.network.request.DetectedObjectsRequest
import com.example.metasearch.core.network.request.NLQueryRequest
import com.example.metasearch.core.network.request.PersonFrequencyRequest
import com.example.metasearch.core.network.request.PersonSearchRequest
import com.example.metasearch.core.network.response.ChangeNameResponse
import com.example.metasearch.core.network.response.DeleteEntityResponse
import com.example.metasearch.core.network.response.PersonFrequencyResponse
import com.example.metasearch.core.network.response.PhotoNameResponse
import com.example.metasearch.core.network.response.PhotoResponse
import com.example.metasearch.core.network.response.TripleResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

public interface WebService {
    @POST("android/circleToSearch")
    suspend fun sendDetectedObjects(
        @Body request: DetectedObjectsRequest,
    ): PhotoResponse

    @POST("/nlqsearch")
    suspend fun sendCypherQuery(
        @Body request: NLQueryRequest
    ): PhotoNameResponse

    @POST("personsearch")
    suspend fun sendPersonData(
        @Body request: PersonSearchRequest,
    ): List<String>

    @POST("changename")
    suspend fun changeName(
        @Body request: ChangeNameRequest,
    ): ChangeNameResponse

    @POST("/api/peoplefrequency")
    suspend fun getPersonFrequency(
        @Body request: PersonFrequencyRequest,
    ): PersonFrequencyResponse

    @GET("/api/photoTripleData/{dbName}/{photoName}")
    suspend fun fetchTripleData(
        @Path("dbName") dbName: String,
        @Path("photoName") photoName: String,
    ): TripleResponse

    @POST("neo4j/deleteEntity/")
    suspend fun deleteEntity(
        @Body request: DeleteEntityRequest,
    ): DeleteEntityResponse

    @Multipart
    @POST("android/uploadimg")
    suspend fun uploadWebAddImage(
        @Part image: MultipartBody.Part,
        @Query("dbName") dbName: String,
    )

    @Multipart
    @POST("android/deleteimg")
    suspend fun uploadWebDeleteImage(
        @Part filename: MultipartBody.Part,
        @Part("dbName") dbName: String,
    )
}
