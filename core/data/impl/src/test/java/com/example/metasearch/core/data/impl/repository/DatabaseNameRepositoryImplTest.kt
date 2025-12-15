package com.example.metasearch.core.data.impl.repository

import com.example.metasearch.core.datastore.api.datasource.DeviceIdDataSource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DatabaseNameRepositoryImplTest {
    @Mock
    private lateinit var mockDataSource: DeviceIdDataSource

    private lateinit var repository: DatabaseNameRepositoryImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = DatabaseNameRepositoryImpl(mockDataSource)
    }

    @Test
    fun getPersistentDeviceDatabaseName_IdNotExists_GeneratesAndSavesId() = runTest {
        whenever(mockDataSource.getDeviceId()).thenReturn("")

        val dbName = repository.getPersistentDeviceDatabaseName()

        assert(dbName.startsWith("db"))

        assertEquals(34, dbName.length)

        verify(mockDataSource).setDeviceId(dbName.substring(2))
    }

    @Test
    fun getPersistentDeviceDatabaseName_IdExists_ReturnsDatabaseName() = runTest {
        val existingUniqueId = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
        whenever(mockDataSource.getDeviceId()).thenReturn(existingUniqueId)

        val dbName = repository.getPersistentDeviceDatabaseName()

        assertEquals("db$existingUniqueId", dbName)

        Mockito.verify(mockDataSource, Mockito.never()).setDeviceId(Mockito.anyString())
    }
}
