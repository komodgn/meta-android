package com.metasearch.android.core.data.impl.repository

import android.net.Uri
import com.metasearch.android.core.data.api.repository.DatabaseNameRepository
import com.metasearch.android.core.data.api.repository.GalleryRepository
import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.core.network.request.OpenAIMessage
import com.metasearch.android.core.network.response.OpenAIChoice
import com.metasearch.android.core.network.response.OpenAIResponse
import com.metasearch.android.core.network.response.PhotoNameResponse
import com.metasearch.android.core.network.service.AIService
import com.metasearch.android.core.network.service.OpenAIService
import com.metasearch.android.core.network.service.WebService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory.times
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {
    @Mock
    private lateinit var mockOpenAIService: OpenAIService

    @Mock
    private lateinit var mockAIService: AIService

    @Mock
    private lateinit var mockWebService: WebService

    @Mock
    private lateinit var mockPersonRepo: PersonRepository

    @Mock
    private lateinit var mockGalleryRepo: GalleryRepository

    @Mock
    private lateinit var mockDatabaseNameRepository: DatabaseNameRepository

    private lateinit var repository: SearchRepositoryImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = SearchRepositoryImpl(
            openAIService = mockOpenAIService,
            webService = mockWebService,
            galleryRepository = mockGalleryRepo,
            aiService = mockAIService,
            personRepository = mockPersonRepo,
            databaseNameRepository = mockDatabaseNameRepository,
        )
    }

    @Test
    fun `동일한 키워드 조합으로 두 번 검색하면 OpenAI는 호출되지만 DB조회는 한 번만 수행된다`() = runTest {
        // Given
        val query1 = "여름 밤에 먹은 음식 찾아줘"
        val query2 = "여름 밤에 찍은 음식 사진"
        val entities = "밤,여름,음식"
        val mockUri = mock(Uri::class.java)

        val mockOpenAIResponse = createMockOpenAIResponse(entities)
        whenever(mockOpenAIService.createChatCompletion(any())).thenReturn(mockOpenAIResponse)

        val mockPhotoResponse = PhotoNameResponse(
            photoNames = listOf("photo1.jpg"),
        )
        whenever(mockDatabaseNameRepository.getPersistentDeviceDatabaseName()).thenReturn("test_db")
        whenever(mockWebService.sendCypherQuery(any())).thenReturn(mockPhotoResponse)
        whenever(mockGalleryRepo.findMatchedUris(any())).thenReturn(listOf(mockUri))

        // When
        repository.nlSearch(query1)
        repository.nlSearch(query2)

        // Then
        verify(mockOpenAIService, times(2)).createChatCompletion(any())
        verify(mockWebService, times(1)).sendCypherQuery(any())
    }

    @Test
    fun `이미지 분석 후 캐시를 비우면 다시 네트워크 검색을 수행한다`() = runTest {
        // Given
        val query = "강아지"
        val entities = "dog"
        val mockUri = mock(Uri::class.java)

        val mockOpenAIResponse = createMockOpenAIResponse(entities)
        val mockPhotoResponse = PhotoNameResponse(
            photoNames = listOf("dog1.jpg"),
        )

        whenever(mockOpenAIService.createChatCompletion(any())).thenReturn(mockOpenAIResponse)
        whenever(mockDatabaseNameRepository.getPersistentDeviceDatabaseName()).thenReturn("dbabf1e5c83b8b4d5da6badd94ebf78ddd")
        whenever(mockWebService.sendCypherQuery(any())).thenReturn(mockPhotoResponse)
        whenever(mockGalleryRepo.findMatchedUris(any())).thenReturn(listOf(mockUri))

        // When
        repository.nlSearch(query)
        repository.clearEntityCache()
        repository.nlSearch(query)

        // Then
        verify(mockOpenAIService, times(2)).createChatCompletion(any())
        verify(mockWebService, times(2)).sendCypherQuery(any())
    }

    private fun createMockOpenAIResponse(content: String) = OpenAIResponse(
        choices = listOf(OpenAIChoice(message = OpenAIMessage(role = "assistant", content = content))),
    )
}
