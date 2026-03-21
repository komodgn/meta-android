package com.metasearch.android.core.data.impl.repository

import android.content.Context
import com.metasearch.android.core.data.api.repository.DatabaseNameRepository
import com.metasearch.android.core.model.Person
import com.metasearch.android.core.network.service.AIService
import com.metasearch.android.core.network.service.WebService
import com.metasearch.android.core.room.api.dao.PersonDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PersonRepositoryImplTest {

    @Mock private lateinit var mockDao: PersonDao

    @Mock private lateinit var mockAiService: AIService

    @Mock private lateinit var mockWebService: WebService

    @Mock private lateinit var mockDbRepo: DatabaseNameRepository

    @Mock private lateinit var mockContext: Context

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PersonRepositoryImpl

    private val fakePerson = Person(id = 1L, name = "test_uuid", inputName = "홍길동")

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // 생성자 주입을 통해 테스트 디스패처 전달
        repository = PersonRepositoryImpl(
            personDao = mockDao,
            aiService = mockAiService,
            webService = mockWebService,
            databaseNameRepository = mockDbRepo,
            ioDispatcher = testDispatcher,
            context = mockContext,
        )
    }

    @Test
    fun `모든 서버 삭제가 성공하면 로컬 DB에서도 인물을 삭제한다`() = runTest(testDispatcher) {
        // Given
        whenever(mockDbRepo.getPersistentDeviceDatabaseName()).thenReturn("db_name")

        // When
        val result = repository.deleteAnalyzedPerson(fakePerson)
        advanceUntilIdle() // 모든 비동기 작업(async)이 완료될 때까지 대기

        // Then
        assertTrue(result.isSuccess)
        verify(mockWebService).deleteEntity(any())
        verify(mockAiService).deletePerson(any(), any())
        verify(mockDao).deletePersonById(fakePerson.id)
    }

    @Test
    fun `서버 삭제 중 하나라도 실패하면 로컬 DB 삭제를 호출하지 않는다`() = runTest(testDispatcher) {
        // Given
        whenever(mockDbRepo.getPersistentDeviceDatabaseName()).thenReturn("db_name")
        // AI 서비스에서 예외 발생 시나리오
        whenever(mockAiService.deletePerson(any(), any())).thenThrow(RuntimeException("AI Server Error"))

        // When
        val result = repository.deleteAnalyzedPerson(fakePerson)
        advanceUntilIdle()

        // Then
        assertTrue(result.isFailure)
        // 서버가 터졌으므로 로컬 DB 삭제는 절대 실행되면 안 됨
        verify(mockDao, never()).deletePersonById(fakePerson.id)
    }
}
