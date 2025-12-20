package com.example.metasearch.core.data.api.repository

import android.net.Uri

interface GalleryRepository {
    suspend fun getAllGalleryImages(): List<Uri>
    suspend fun getFileName(uri: Uri): String?
}
