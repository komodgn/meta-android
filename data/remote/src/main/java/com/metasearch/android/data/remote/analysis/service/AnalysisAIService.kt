package com.metasearch.android.data.remote.analysis.service

import com.metasearch.android.data.remote.analysis.response.CommonResponse
import com.metasearch.android.data.remote.analysis.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AnalysisAIService {
    @Multipart
    @POST("android/upload_add")
    suspend fun uploadAddImage(
        @Part addImage: MultipartBody.Part,
        @Part("dbName") dbName: RequestBody,
    ): CommonResponse

    @Multipart
    @POST("android/upload_delete")
    suspend fun uploadDeleteImage(
        @Part filename: MultipartBody.Part,
        @Part("dbName") dbName: RequestBody,
    ): CommonResponse

    @Multipart
    @POST("android/upload_database")
    suspend fun uploadDatabaseImage(
        @Part filename: MultipartBody.Part,
        @Part("dbName") dbName: RequestBody,
    )

    @Multipart
    @POST("android/upload_first")
    suspend fun uploadFirst(
        @Part("first") first: RequestBody,
        @Part("dbName") dbName: RequestBody,
    )

    @Multipart
    @POST("android/upload_finish")
    suspend fun uploadFinish(
        @Part("finish") finish: RequestBody,
        @Part("dbName") dbName: RequestBody,
        @Part("rowCount") rowCount: RequestBody,
    ): UploadResponse
}
