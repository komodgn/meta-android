package com.metasearch.android.data.remote.analysis.service

import com.metasearch.android.data.remote.analysis.request.DeleteImageRequest
import com.metasearch.android.data.remote.analysis.response.TripleResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AnalysisWebService {
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

    @GET("api/photoTripleData/{dbName}/{photoName}")
    suspend fun fetchTripleData(
        @Path("dbName") dbName: String,
        @Path("photoName") photoName: String,
    ): TripleResponse
}
