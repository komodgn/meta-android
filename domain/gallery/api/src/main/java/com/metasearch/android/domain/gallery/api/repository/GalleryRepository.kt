package com.metasearch.android.domain.gallery.api.repository

import androidx.paging.PagingData
import com.metasearch.android.data.domain.GalleryImage
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getGalleryPagingData(): Flow<PagingData<GalleryImage>>

    suspend fun getAllGalleryImageUris(): List<String>
    suspend fun getFileName(uriString: String): String?
    suspend fun findMatchedUri(photoName: String): String?
    suspend fun findMatchedUris(photoNames: List<String>): List<String>
}
