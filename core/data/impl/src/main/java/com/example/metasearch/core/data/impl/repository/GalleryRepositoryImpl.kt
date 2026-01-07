package com.example.metasearch.core.data.impl.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.metasearch.core.data.api.repository.GalleryRepository
import com.example.metasearch.core.data.impl.datasource.GalleryPagingSource
import com.example.metasearch.core.data.impl.di.IoDispatcher
import com.example.metasearch.core.model.GalleryImageModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GalleryRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
) : GalleryRepository {

    override fun getGalleryPagingData(): Flow<PagingData<GalleryImageModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                initialLoadSize = 60,
            ),
            pagingSourceFactory = { GalleryPagingSource(context, ioDispatcher) },
        ).flow
    }

    override suspend fun getAllGalleryImages(): List<Uri> = withContext(Dispatchers.IO) {
        val imageUris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC",
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                imageUris.add(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id))
            }
        }
        imageUris
    }

    override suspend fun getFileName(uri: Uri): String? = withContext(Dispatchers.IO) {
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
            } else {
                null
            }
        }
    }

    override suspend fun findMatchedUri(photoName: String): Uri? = withContext(Dispatchers.IO) {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(photoName)

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null,
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            } else {
                null
            }
        }
    }

    override suspend fun findMatchedUris(photoNames: List<String>): List<Uri> = withContext(Dispatchers.IO) {
        val allImages = mutableMapOf<String, Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME)

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null,
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                allImages[name] = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            }
        }
        photoNames.mapNotNull { allImages[it] }
    }
}
