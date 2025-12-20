package com.example.metasearch.core.data.impl.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.metasearch.core.data.api.repository.GalleryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GalleryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : GalleryRepository {
    override suspend fun getAllGalleryImages(): List<Uri> = withContext(Dispatchers.IO) {
        val imageUris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                imageUris.add(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id))
            }
        }
        imageUris
    }

    override suspend fun getFileName(uri: Uri): String? = withContext(Dispatchers.IO){
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null,
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                } else null
        }
    }

}
