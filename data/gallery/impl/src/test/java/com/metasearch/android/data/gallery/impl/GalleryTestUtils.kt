package com.metasearch.android.data.gallery.impl

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

object GalleryTestUtils {

    /**
     * Inserts a mock image into the [MediaStore] and returns its [Uri].
     * * @param fileName The display name of the mock image.
     * @param dateAdded Optional timestamp for sorting tests.
     * @return The [Uri] of the inserted mock image.
     */
    fun insertMockImage(
        context: Context,
        fileName: String,
        dateAdded: Long? = null,
    ): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            dateAdded?.let { put(MediaStore.Images.Media.DATE_ADDED, it) }
        }
        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues,
        )
    }
}
