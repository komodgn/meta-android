package com.example.metasearch.core.network.service

import com.example.metasearch.core.network.request.FocusingSearchRequest
import com.example.metasearch.core.network.response.CircleDetectionResponse
import com.example.metasearch.core.network.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
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
    fun deletePerson(@Part("dbName") dbName: RequestBody?, @Part("deletePerson") deletePerson: RequestBody?): Call<Void?>?

    @Multipart
    @POST("android/upload_add")
    fun uploadAddImage(@Part image: MultipartBody.Part?, @Part("dbName") dbName: RequestBody?): Call<Void?>?

    @Multipart
    @POST("android/upload_delete")
    fun uploadDeleteImage(@Part filename: MultipartBody.Part?, @Part("dbName") dbName: RequestBody?): Call<Void?>?

    @Multipart
    @POST("android/upload_database")
    fun uploadDatabaseImage(@Part filename: MultipartBody.Part?, @Part("dbName") dbName: RequestBody?): Call<Void?>?

    @Multipart
    @POST("android/upload_first")
    fun uploadFirst(@Part("first") first: RequestBody?, @Part("dbName") dbName: RequestBody?): Call<Void?>?

    @Multipart
    @POST("android/upload_finish")
    fun uploadFinish(
        @Part("finish") finish: RequestBody?,
        @Part("dbName") dbName: RequestBody?,
        @Part("rowCount") rowCount: RequestBody?,
    ): Call<UploadResponse?>?

//    @Multipart
//    @POST("android/upload_person_name")
//    fun upload_person_name(
//        @Part("dbName") dbName: RequestBody?,
//        @Part("oldName") oldName: RequestBody?,
//        @Part("newName") newName: RequestBody?,
//    ): Call<Void?>?
}
