package com.example.metasearch.core.data.api.repository

import android.net.Uri

interface GalleryRepository {
    suspend fun getAllGalleryImages(): List<Uri>
    suspend fun getAllGalleryPaths(): List<String>
    suspend fun getFileName(uri: Uri): String?
    suspend fun findMatchedUri(photoName: String): Uri?
    suspend fun findMatchedUris(photoNames: List<String>): List<Uri>
}
