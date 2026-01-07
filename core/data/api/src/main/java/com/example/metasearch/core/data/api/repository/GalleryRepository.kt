package com.example.metasearch.core.data.api.repository

import android.net.Uri
import androidx.paging.PagingData
import com.example.metasearch.core.model.GalleryImageModel
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getGalleryPagingData(): Flow<PagingData<GalleryImageModel>>

    suspend fun getAllGalleryImages(): List<Uri>
    suspend fun getFileName(uri: Uri): String?
    suspend fun findMatchedUri(photoName: String): Uri?
    suspend fun findMatchedUris(photoNames: List<String>): List<Uri>
}
