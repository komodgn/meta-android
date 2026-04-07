package com.metasearch.android.data.person.impl.repository

import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.core.network.request.ChangeNameRequest
import com.metasearch.android.core.network.request.DeleteEntityRequest
import com.metasearch.android.core.network.request.PersonFrequencyRequest
import com.metasearch.android.core.network.request.PersonSearchRequest
import com.metasearch.android.core.network.service.AIService
import com.metasearch.android.core.network.service.WebService
import com.metasearch.android.core.room.api.dao.PersonDao
import com.metasearch.android.core.room.api.entity.FaceEntity
import com.metasearch.android.data.domain.Person
import com.metasearch.android.data.domain.PersonFrequency
import com.metasearch.android.data.person.impl.mapper.toModel
import com.metasearch.android.domain.device.api.repository.DatabaseNameRepository
import com.metasearch.android.domain.person.api.repository.PersonRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@SingleIn(DataScope::class)
@Inject
class PersonRepositoryImpl(
    private val personDao: PersonDao,
    private val databaseNameRepository: DatabaseNameRepository,
    private val webService: WebService,
    private val aiService: AIService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : PersonRepository {

    override fun getAllPersons(): Flow<List<Person>> = personDao
        .getAllPersonsWithFaces().map { list -> list.map { it.toModel(emptyMap()) } }

    override fun getPersonById(personId: Long): Flow<Person?> =
        personDao.getPersonWithFacesFlow(personId)
            .map { it?.toModel(emptyMap()) }

    override suspend fun getInputNameBySystemName(systemName: String): String? = withContext(ioDispatcher) {
        personDao.getInputNameByImageName(systemName)
    }

    override suspend fun getPersonCount(): Int = personDao.getPersonCount()

    override suspend fun getPersonIdByImageName(imageName: String): Long? =
        personDao.findPersonIdByImageName(imageName)

    override suspend fun addFaceToExistingPerson(personId: Long, imageName: String, imageBytes: ByteArray): Long {
        val currentPerson = personDao.getPersonById(personId)
        val actualName = currentPerson?.inputName ?: imageName

        return personDao.insertFace(
            FaceEntity(
                personId = personId,
                imageName = actualName,
                imageData = imageBytes,
            ),
        )
    }

    override suspend fun addAnalyzedPerson(imageName: String, imageBytes: ByteArray) =
        personDao.insertPersonAndFace(imageName, imageBytes)

    override suspend fun getMismatchedFaceNames(): List<Pair<String, String>> =
        personDao.getMismatchedFaceNames().map {
            it.serverName to it.actualName
        }

    override suspend fun isNameExists(inputName: String): Boolean =
        personDao.isNameExists(inputName)

    override suspend fun updateRepresentativeFace(personId: Long, faceId: Long): Result<Unit> = runSuspendCatching {
        personDao.updateRepresentativeFace(personId, faceId)
    }

    override suspend fun updatePersonInfo(
        personId: Long,
        newName: String,
        newPhone: String,
        isHome: Boolean,
        faceId: Long?,
    ): Result<Long> = runSuspendCatching {
        val currentPersonEntity = personDao.getPersonById(personId)
        val oldName = currentPersonEntity?.inputName ?: ""
        val targetPersonId = personDao.getPersonIdByName(newName)

        if (targetPersonId != null && targetPersonId != personId) {
            personDao.mergePersons(personId, targetPersonId)
            changePersonNameOnServer(oldName, newName).getOrThrow()
            targetPersonId
        } else {
            personDao.updatePersonFullInfo(personId, newName, newPhone, isHome, faceId)

            if (oldName != newName && oldName.isNotEmpty()) {
                changePersonNameOnServer(oldName, newName)
            }
            personId
        }
    }

    override suspend fun changePersonNameOnServer(oldName: String, newName: String): Result<Unit> = runSuspendCatching {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
        webService.changePersonName(ChangeNameRequest(dbName, oldName, newName))
        Unit
    }

    override suspend fun getPersonPhotoNames(personName: String): Result<List<String>> = runSuspendCatching {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
        webService.sendPersonData(PersonSearchRequest(dbName, personName))
    }

    override suspend fun fetchPhotoFrequencies(names: List<String>): Result<List<PersonFrequency>> = runSuspendCatching {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        val response = webService.getPersonFrequency(PersonFrequencyRequest(dbName, names))
        response.toModel()
    }

    override suspend fun deleteFromWebService(inputName: String): Result<Unit> = runSuspendCatching {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
        webService.deleteEntity(DeleteEntityRequest(dbName, inputName))
        Unit
    }

    override suspend fun deleteFromAiService(personName: String): Result<Unit> = runSuspendCatching {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        aiService.deletePerson(
            dbName.toRequestBody("text/plain".toMediaTypeOrNull()),
            personName.toRequestBody("text/plain".toMediaTypeOrNull()),
        )
    }

    override suspend fun deleteFromLocalDb(personId: Long): Result<Unit> = runSuspendCatching {
        personDao.deletePersonById(personId)
    }
}
