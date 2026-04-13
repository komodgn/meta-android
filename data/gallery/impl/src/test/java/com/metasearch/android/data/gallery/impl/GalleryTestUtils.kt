package com.metasearch.android.data.gallery.impl

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

object GalleryTestUtils {

    /**
     * Inserts a mock image into the [MediaStore] and returns its [Uri].
     * @param fileName The display name of the mock image.
     * @param dateAdded Optional timestamp for sorting tests.
     * @param insertedUris A mutable list to track created [Uri]s for later cleanup in @After.
     * @return The [Uri] of the inserted mock image.
     * @throws AssertionError if the insertion fails.
     */
    fun insertMockImage(
        context: Context,
        fileName: String,
        dateAdded: Long? = null,
        insertedUris: MutableList<Uri>? = null,
    ): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            dateAdded?.let { put(MediaStore.Images.Media.DATE_ADDED, it) }
        }
        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues,
        )

        if (uri == null) {
            throw AssertionError("Failed to insert mock image into MediaStore")
        }

        uri.let { insertedUris?.add(it) }
        return uri
    }

    fun cleanup(context: Context, insertedUris: List<Uri>) {
        insertedUris.forEach { uri ->
            context.contentResolver.delete(uri, null, null)
        }
    }
}
