package com.metasearch.android.data.search.impl

import com.google.common.truth.Truth.assertThat
import com.metasearch.android.data.remote.search.SearchClient
import com.metasearch.android.data.remote.search.response.CircleDetectionResponse
import com.metasearch.android.data.remote.search.response.PhotoNameResponse
import com.metasearch.android.data.remote.search.response.PhotoResponse
import com.metasearch.android.data.remote.search.response.Photos
import com.metasearch.android.data.remote.search.util.CypherQueryGenerator.generateQueryByKeywords
import com.metasearch.android.data.search.impl.repository.SearchRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.File

class SearchRepositoryImplTest {

    private val searchClient: SearchClient = mock()
    private lateinit var repository: SearchRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = SearchRepositoryImpl(searchClient)
    }

    @Test
    fun `analyzeFocusingImage - Success`() = runTest {
        val mockResponse = CircleDetectionResponse(
            message = "success",
            detectedObjects = listOf("고양이", "강아지"),
        )

        whenever(searchClient.analyzeFocusingImage(any(), any(), any())).thenReturn(mockResponse)

        val result = repository.analyzeFocusingImage("dbabf1e5c83b8b485da6ba3b94ebf78dgs", File("test"), emptyList())

        assertEquals(2, result.size)
        assertEquals("고양이", result[0])
    }

    @Test
    fun `analyzeFocusingImage - Failure`() = runTest {
        whenever(searchClient.analyzeFocusingImage(any(), any(), any())).thenThrow(RuntimeException())

        val result = repository.analyzeFocusingImage("dbabf1e5c83b8b485da6ba3b94ebf78dgs", File("test"), emptyList())

        assertEquals(0, result.size)
    }

    @Test
    fun `extractKeywordsFromNL - Success`() = runTest {
        val mockKeywords = listOf("바다", "여름")
        whenever(searchClient.extractKeywords(any())).thenReturn(mockKeywords)

        val result = repository.extractKeywordsFromNL("여름 바다 여행 사진 찾아줘")

        assertEquals(2, result.size)
        assertEquals("바다", result[0])
    }

    @Test
    fun `extractKeywordsFromNL - Failure`() = runTest {
        whenever(searchClient.extractKeywords(any())).thenThrow(RuntimeException())

        val result = repository.extractKeywordsFromNL("query")

        assertEquals(0, result.size)
    }

    @Test
    fun `findPhotosByDetectedObjects - Success`() = runTest {
        val mockPhotos = Photos(
            commonPhotos = listOf("common_01.jpg"),
            individualPhotos = mapOf(
                "강아지" to listOf("dog_01.jpg", "dog_02.jpg"),
                "고양이" to listOf("cat_01.jpg"),
            ),
        )
        val mockResponse = PhotoResponse(photos = mockPhotos)
        whenever(searchClient.fetchPhotosByObjects(any(), any())).thenReturn(mockResponse)

        val result = repository.findPhotosByDetectedObjects("dbabf1e5c83b8b485da6ba3b94ebf78dgs", listOf("dog", "cat"))

        assertEquals(3, result.groups.size)
        val dogGroup = result.groups.find { it.categoryName == "강아지" }
        assertNotNull(dogGroup)
        assertEquals(2, dogGroup?.photoNames?.size)
    }

    @Test
    fun `searchPhotosByKeywords - Success`() = runTest {
        val mockPhotoNames = listOf("cat.jpg", "dog.jpg")
        val mockResponse = PhotoNameResponse(photoNames = mockPhotoNames)

        whenever(searchClient.fetchPhotosByKeywords(any(), any())).thenReturn(mockResponse)

        val result = repository.searchPhotosByKeywords("dbabf1e5c83b8b485da6ba3b94ebf78dgs", listOf("keyword1"))

        assertEquals(2, result.size)
        assertEquals("cat.jpg", result[0])
        verify(searchClient, atLeastOnce()).fetchPhotosByKeywords(any(), any())
    }

    @Test
    fun `searchPhotosByKeywords - Failure`() = runTest {
        whenever(searchClient.fetchPhotosByKeywords(any(), any())).thenThrow(RuntimeException())

        val result = repository.searchPhotosByKeywords("dbabf1e5c83b8b485da6ba3b94ebf78dgs", listOf("keyword"))

        assertEquals(0, result.size)
    }

    @Test
    fun `generateQueryByKeywords - should generate correct Cypher query for multiple keywords`() {
        // given
        val keywords = listOf("안경", "모자")

        // when
        val query = generateQueryByKeywords(keywords)

        // then
        val expected = "MATCH (photo)-[]->(a0 {name: '안경'}), (photo)-[]->(a1 {name: '모자'}) RETURN DISTINCT photo.name AS PhotoName"
        assertThat(query).isEqualTo(expected)
    }
}
