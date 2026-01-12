package com.metasearch.android.core.network.service

import com.metasearch.android.core.network.request.ChangeNameRequest
import com.metasearch.android.core.network.request.DeleteEntityRequest
import com.metasearch.android.core.network.request.DeleteImageRequest
import com.metasearch.android.core.network.request.DetectedObjectsRequest
import com.metasearch.android.core.network.request.NLQueryRequest
import com.metasearch.android.core.network.request.PersonFrequencyRequest
import com.metasearch.android.core.network.request.PersonSearchRequest
import com.metasearch.android.core.network.response.ChangeNameResponse
import com.metasearch.android.core.network.response.DeleteEntityResponse
import com.metasearch.android.core.network.response.PersonFrequencyResponse
import com.metasearch.android.core.network.response.PhotoNameResponse
import com.metasearch.android.core.network.response.PhotoResponse
import com.metasearch.android.core.network.response.TripleResponse
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
        @Body request: NLQueryRequest,
    ): PhotoNameResponse

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

    @GET("api/photoTripleData/{dbName}/{photoName}")
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

    @POST("deleteImage/")
    suspend fun uploadWebDeleteImage(
        @Body request: DeleteImageRequest,
    )
}
