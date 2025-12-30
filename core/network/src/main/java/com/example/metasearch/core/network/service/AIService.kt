package com.example.metasearch.core.network.service

import com.example.metasearch.core.network.request.FocusingSearchRequest
import com.example.metasearch.core.network.response.CircleDetectionResponse
import com.example.metasearch.core.network.response.CommonResponse
import com.example.metasearch.core.network.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

public interface AIService {
    @Multipart
    @POST("android/circle_search")
    suspend fun uploadImageAndCircles(
        @Part image: MultipartBody.Part,
        @Part("dbName") dbName: RequestBody,
        @Part("circles") request: FocusingSearchRequest,
    ): CircleDetectionResponse

    @Multipart
    @POST("android/delete_person")
    suspend fun deletePerson(
        @Part("dbName") dbName: RequestBody,
        @Part("deletePerson") deletePerson: RequestBody,
    )

    @Multipart
    @POST("android/upload_add")
    suspend fun uploadAddImage(
        @Part addImage: MultipartBody.Part,
        @Part("dbName") dbName: RequestBody,
    ): CommonResponse

    @Multipart
    @POST("android/upload_delete")
    suspend fun uploadDeleteImage(
        @Part("deleteImage") filename: MultipartBody.Part,
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
