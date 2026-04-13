package com.metasearch.android.data.gallery.impl

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.test.core.app.ApplicationProvider
import com.metasearch.android.data.gallery.impl.repository.GalleryRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GalleryRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var repository: GalleryRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = GalleryRepositoryImpl(testDispatcher, context)
    }

    @Test
    fun `getAllGalleryImageUris - returns all URI strings when photos exist in gallery`() = runTest(testDispatcher) {
        // given
        insertMockImage("image1.jpg")
        insertMockImage("image2.jpg")

        // when
        val result = repository.getAllGalleryImageUris()

        // then
        assertEquals(2, result.size)
    }

    @Test
    fun `getFileName - returns correct display name for a given URI string`() = runTest(testDispatcher) {
        // given
        val expectedName = "my_special_photo.jpg"
        val uri = insertMockImageAndGetUri(expectedName)

        // when
        val resultName = repository.getFileName(uri.toString())

        // then
        assertEquals(expectedName, resultName)
    }

    @Test
    fun `getFileName - returns null when URI does not exist`() = runTest(testDispatcher) {
        // given
        val nonExistentUri = "content://media/external/images/media/999"

        // when
        val resultName = repository.getFileName(nonExistentUri)

        // then
        assertEquals(null, resultName)
    }

    @Test
    fun `findMatchedUri - returns the exact URI string for a matching filename`() = runTest(testDispatcher) {
        // given
        val targetName = "target_photo.jpg"
        // Store the actual Uri returned upon insertion to compare with the result string (e.g., content://media/external/images/media/1)
        val expectedUri = insertMockImageAndGetUri(targetName)
        insertMockImage("other_photo.jpg")

        // when
        val result = repository.findMatchedUri(targetName)

        // then
        assertEquals(expectedUri.toString(), result)
    }

    @Test
    fun `findMatchedUris - returns list of matching URIs for multiple filenames`() = runTest(testDispatcher) {
        // given
        val name1 = "photo_1.png"
        val name2 = "photo_2.png"
        val name3 = "not_to_be_found.png"

        val uri1 = insertMockImageAndGetUri(name1)
        val uri2 = insertMockImageAndGetUri(name2)

        // when
        val searchNames = listOf(name1, name2, name3)
        val results = repository.findMatchedUris(searchNames)

        // then
        assertEquals(2, results.size)
        assertTrue(results.contains(uri1.toString()))
        assertTrue(results.contains(uri2.toString()))
    }

    /**
     * Helper function to insert mock data and return its actual URI for verification.
     */
    private fun insertMockImageAndGetUri(fileName: String): android.net.Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    /**
     * Helper function to insert mock data into Robolectric's ShadowContentResolver.
     */
    private fun insertMockImage(fileName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            // Additional fields like DATE_ADDED can be added here if needed
        }
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
}
