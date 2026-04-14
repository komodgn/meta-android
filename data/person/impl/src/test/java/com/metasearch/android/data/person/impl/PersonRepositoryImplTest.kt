package com.metasearch.android.data.person.impl

import com.metasearch.android.core.room.api.dao.PersonDao
import com.metasearch.android.data.person.impl.repository.PersonRepositoryImpl
import com.metasearch.android.data.remote.person.PersonClient
import com.metasearch.android.domain.device.api.repository.DatabaseNameRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PersonRepositoryImplTest {

    private val personDao: PersonDao = mock()
    private val databaseNameRepository: DatabaseNameRepository = mock()
    private val personClient: PersonClient = mock()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PersonRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = PersonRepositoryImpl(
            personDao,
            databaseNameRepository,
            personClient,
            testDispatcher,
        )
    }

    @Test
    fun `updatePersonInfo - should merge persons when duplicate name exists`() = runTest(testDispatcher) {
        // given
        val personId = 1L
        val targetPersonId = 2L
        val oldName = "OldName"
        val newName = "ExistingName"
        val dbName = "dbabf1e5c83b8b485da6ba3b94ebf78dgs"

        whenever(personDao.getPersonById(personId)).thenReturn(
            PersonTestUtil.fakePersonEntity(id = personId, inputName = oldName),
        )
        whenever(personDao.getPersonIdByName(newName)).thenReturn(targetPersonId)
        whenever(databaseNameRepository.getPersistentDeviceDatabaseName()).thenReturn(dbName)
        whenever(personClient.changePersonName(dbName, oldName, newName)).thenReturn(Unit)

        // when
        val result = repository.updatePersonInfo(personId, newName, "010-1234-0000", false, null)

        // then
        verify(personDao).mergePersons(personId, targetPersonId)
        verify(personClient).changePersonName(dbName, oldName, newName)
        assertEquals(targetPersonId, result.getOrNull())
    }

    @Test
    fun `updatePersonInfo - should update info when name is not duplicated`() = runTest(testDispatcher) {
        // given
        val personId = 1L
        val oldName = "OldName"
        val newName = "NewName"
        val dbName = "dbabf1e5c83b8b485da6ba3b94ebf78dgs"

        whenever(personDao.getPersonById(personId)).thenReturn(
            PersonTestUtil.fakePersonEntity(id = personId, inputName = oldName),
        )
        whenever(personDao.getPersonIdByName(newName)).thenReturn(null)
        whenever(databaseNameRepository.getPersistentDeviceDatabaseName()).thenReturn(dbName)
        whenever(personClient.changePersonName(dbName, oldName, newName)).thenReturn(Unit)

        // when
        val result = repository.updatePersonInfo(personId, newName, "010-5678-0000", true, 10L)

        // then
        verify(personDao).updatePersonFullInfo(personId, newName, "010-5678", true, 10L)
        verify(personClient).changePersonName(dbName, oldName, newName)
        assertEquals(personId, result.getOrNull())
    }
}
