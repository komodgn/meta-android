package com.metasearch.android.core.data.api.repository

import android.net.Uri
import androidx.paging.PagingData
import com.metasearch.android.core.model.GalleryImage
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getGalleryPagingData(): Flow<PagingData<GalleryImage>>

    suspend fun getAllGalleryImages(): List<Uri>
    suspend fun getFileName(uri: Uri): String?
    suspend fun findMatchedUri(photoName: String): Uri?
    suspend fun findMatchedUris(photoNames: List<String>): List<Uri>
}
