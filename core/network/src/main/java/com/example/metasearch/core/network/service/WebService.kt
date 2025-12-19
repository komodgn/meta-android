package com.example.metasearch.core.network.service

import com.example.metasearch.core.network.request.ChangeNameRequest
import com.example.metasearch.core.network.request.DeleteEntityRequest
import com.example.metasearch.core.network.request.DetectedObjectsRequest
import com.example.metasearch.core.network.request.NLQueryRequest
import com.example.metasearch.core.network.request.PersonFrequencyRequest
import com.example.metasearch.core.network.response.ChangeNameResponse
import com.example.metasearch.core.network.response.DeleteEntityResponse
import com.example.metasearch.core.network.response.PersonFrequencyResponse
import com.example.metasearch.core.network.response.PhotoNameResponse
import com.example.metasearch.core.network.response.PhotoResponse
import com.example.metasearch.core.network.response.TripleResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

public interface WebService {
    @POST("android/circleToSearch")
    fun sendDetectedObjects(
        @Body request: DetectedObjectsRequest,
    ): PhotoResponse

    @POST("/nlqsearch")
    suspend fun sendCypherQuery(
        @Body request: NLQueryRequest
    ): PhotoNameResponse

    @POST("personsearch")
    fun sendPersonData(@Body body: RequestBody?): Call<MutableList<String?>?>?

    @POST("changename")
    fun changeName(@Body request: ChangeNameRequest?): Call<ChangeNameResponse?>?

    @POST("/api/peoplefrequency")
    fun getPersonFrequency(@Body request: PersonFrequencyRequest?): Call<PersonFrequencyResponse?>?

    @GET("/api/photoTripleData/{dbName}/{photoName}")
    fun fetchTripleData(@Path("dbName") dbName: String?, @Path("photoName") photoName: String?): Call<TripleResponse?>?

    @POST("neo4j/deleteEntity/")
    fun deleteEntity(@Body request: DeleteEntityRequest?): Call<DeleteEntityResponse?>?

    @Multipart
    @POST("android/uploadimg")
    fun uploadWebAddImage(@Part image: MultipartBody.Part?, @Query("dbName") dbName: String?): Call<Void?>?

    @Multipart
    @POST("android/deleteimg")
    fun uploadWebDeleteImage(@Part filename: MultipartBody.Part?, @Part("dbName") dbName: RequestBody?): Call<Void?>?
}
