package com.metasearch.android.data.gallery.impl.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.metasearch.android.core.di.annotation.IoDispatcher
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.domain.GalleryImage
import com.metasearch.android.data.gallery.impl.GalleryPagingSource
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@SingleIn(DataScope::class)
@Inject
class GalleryRepositoryImpl(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val context: Context,
) : GalleryRepository {

    override fun getGalleryPagingData(): Flow<PagingData<GalleryImage>> {
        return Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            pagingSourceFactory = { GalleryPagingSource(context, ioDispatcher) },
        ).flow
    }

    override suspend fun getAllGalleryImages(): List<String> = withContext(ioDispatcher) {
        val imageUris = mutableListOf<String>()
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
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageUris.add(uri.toString())
            }
        }
        imageUris
    }

    override suspend fun getFileName(uriString: String): String? = withContext(ioDispatcher) {
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        context.contentResolver.query(
            uriString.toUri(),
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

    override suspend fun findMatchedUri(photoName: String): String? = withContext(ioDispatcher) {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} = ?"
        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            arrayOf(photoName),
            null,
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id).toString()
            } else null
        }
    }

    override suspend fun findMatchedUris(photoNames: List<String>): List<String> = withContext(ioDispatcher) {
        val allImages = mutableMapOf<String, String>()
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME)

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, null, null
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                allImages[name] = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id).toString()
            }
        }
        photoNames.mapNotNull { allImages[it] }
    }
}
