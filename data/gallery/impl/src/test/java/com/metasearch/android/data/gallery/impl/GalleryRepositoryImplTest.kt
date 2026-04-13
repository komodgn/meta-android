package com.metasearch.android.data.gallery.impl

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.metasearch.android.data.gallery.impl.GalleryTestUtils.insertMockImage
import com.metasearch.android.data.gallery.impl.repository.GalleryRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    private val insertedUris = mutableListOf<Uri>()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = GalleryRepositoryImpl(testDispatcher, context)
    }

    @After
    fun tearDown() {
        GalleryTestUtils.cleanup(context, insertedUris)
        insertedUris.clear()
    }

    @Test
    fun `getGalleryPagingData emits PagingData when subscribed`() = runTest(testDispatcher) {
        // given
        insertMockImage(context, "test.jpg", dateAdded = 1000L, insertedUris = insertedUris)

        // when
        val flow = repository.getGalleryPagingData()

        // then
        val result = flow.first()
        assertNotNull(result)
    }

    @Test
    fun `getAllGalleryImageUris - returns all URI strings when photos exist in gallery`() = runTest(testDispatcher) {
        // given
        insertMockImage(context, "image1.jpg", insertedUris = insertedUris)
        insertMockImage(context, "image2.jpg", insertedUris = insertedUris)

        // when
        val result = repository.getAllGalleryImageUris()

        // then
        assertEquals(2, result.size)
    }

    @Test
    fun `getFileName - returns correct display name for a given URI string`() = runTest(testDispatcher) {
        // given
        val expectedName = "my_special_photo.jpg"
        val uri = insertMockImage(context, expectedName, insertedUris = insertedUris)

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
        val expectedUri = insertMockImage(context, targetName, insertedUris = insertedUris)
        insertMockImage(context, "other_photo.jpg", insertedUris = insertedUris)

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

        val uri1 = insertMockImage(context, name1, insertedUris = insertedUris)
        val uri2 = insertMockImage(context, name2, insertedUris = insertedUris)

        // when
        val searchNames = listOf(name1, name2, name3)
        val results = repository.findMatchedUris(searchNames)

        // then
        assertEquals(2, results.size)
        assertTrue(results.contains(uri1.toString()))
        assertTrue(results.contains(uri2.toString()))
    }
}
