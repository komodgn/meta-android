package com.metasearch.android.data.gallery.impl

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.test.core.app.ApplicationProvider
import com.metasearch.android.data.domain.GalleryImage
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
class GalleryPagingSourceTest {

    private lateinit var context: Context
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var pagingSource: GalleryPagingSource

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        pagingSource = GalleryPagingSource(context, testDispatcher)
    }

    @Test
    fun `load - returns success page when data exists`() = runTest(testDispatcher) {
        // given
        repeat(5) { i -> insertMockImage("image_$i.jpg", dateAdded = 1000L + i) }

        // when
        val params = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 3,
            placeholdersEnabled = false,
        )
        val result = pagingSource.load(params)

        // then
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, GalleryImage>
        assertEquals(3, page.data.size)
        assertEquals(null, page.prevKey)
        assertEquals(3, page.nextKey)
    }

    @Test
    fun `load - returns end of pagination when no more data`() = runTest(testDispatcher) {
        // given
        repeat(2) { i -> insertMockImage("image_$i.jpg", dateAdded = 1000L + i) }

        // when
        val params = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 5,
            placeholdersEnabled = false,
        )
        val result = pagingSource.load(params)

        // then
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, GalleryImage>

        assertEquals(2, page.data.size)
        assertEquals(null, page.nextKey)
    }

    @Test
    fun `load - orders images by date added descending`() = runTest(testDispatcher) {
        // given
        insertMockImage("old.jpg", dateAdded = 1000L)
        insertMockImage("new.jpg", dateAdded = 2000L)

        // when
        val params = PagingSource.LoadParams.Refresh<Int>(key = null, loadSize = 2, placeholdersEnabled = false)
        val result = pagingSource.load(params)
        assertTrue(result is PagingSource.LoadResult.Page)

        val page = result as PagingSource.LoadResult.Page<Int, GalleryImage>

    private fun insertMockImage(fileName: String, dateAdded: Long) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.DATE_ADDED, dateAdded)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        // then
        assertEquals(2000L, page.data[0].dateAdded)
        assertEquals(1000L, page.data[1].dateAdded)
    }
}
