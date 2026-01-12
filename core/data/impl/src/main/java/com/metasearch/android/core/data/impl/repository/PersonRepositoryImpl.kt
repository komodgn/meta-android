package com.metasearch.android.core.data.impl.repository

import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.util.Log
import com.metasearch.android.core.common.utils.normalizePhoneNumber
import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.data.api.repository.DatabaseNameRepository
import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.core.data.impl.di.IoDispatcher
import com.metasearch.android.core.data.impl.mapper.toModel
import com.metasearch.android.core.model.PersonModel
import com.metasearch.android.core.network.request.ChangeNameRequest
import com.metasearch.android.core.network.request.DeleteEntityRequest
import com.metasearch.android.core.network.request.PersonFrequencyRequest
import com.metasearch.android.core.network.request.PersonSearchRequest
import com.metasearch.android.core.network.service.AIService
import com.metasearch.android.core.network.service.WebService
import com.metasearch.android.core.room.api.dao.PersonDao
import com.metasearch.android.core.room.api.entity.FaceEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val personDao: PersonDao,
    private val aiService: AIService,
    private val webService: WebService,
    private val databaseNameRepository: DatabaseNameRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    @ApplicationContext private val context: Context,
) : PersonRepository {
    private val tag = "PersonRepoImpl"

    private fun getCallDurations(): Map<String, Long> {
        val callLogDuration = mutableMapOf<String, Long>()

        val cursor: Cursor? = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.DURATION),
            null,
            null,
            null,
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

            if (numberIndex >= 0 && durationIndex >= 0) {
                while (it.moveToNext()) {
                    val number = it.getString(numberIndex)
                    val duration = it.getLong(durationIndex)
                    val normalizedNumber = normalizePhoneNumber(number)

                    callLogDuration[normalizedNumber] = callLogDuration.getOrDefault(normalizedNumber, 0L) + duration
                }
            }
        }
        return callLogDuration
    }

    private fun normalizeScores(people: List<PersonModel>): List<PersonModel> {
        if (people.isEmpty()) return emptyList()
        val maxPhotoCount = people.maxOf { it.photoCount }.coerceAtLeast(1)
        val maxDuration = people.maxOf { it.totalDuration }.coerceAtLeast(1L)

        return people.map { person ->
            val normalizedScore = ((person.photoCount.toDouble() / maxPhotoCount) + (person.totalDuration.toDouble() / maxDuration)) / 2.0
            person.copy(normalizedScore = normalizedScore)
        }.sortedByDescending { it.normalizedScore }
    }

    override fun getAllPersons(): Flow<List<PersonModel>> = personDao
        .getAllPersonsWithFaces().map { personWithFacesList ->
            val callDurations = getCallDurations()

            personWithFacesList.map { it.toModel(callDurations) }
        }

    override fun getHomeDisplayPersons(): Flow<List<PersonModel>> = personDao
        .getPersonsWithFaces().map { personWithFacesList ->
            val callDurations = getCallDurations()

            val models = personWithFacesList
                .filter { it.person.isHomeDisplay }
                .map { it.toModel(callDurations) }

            normalizeScores(models)
        }

    override fun getPersonById(personId: Long): Flow<PersonModel?> =
        personDao.getPersonWithFacesFlow(personId)
            .map { it?.toModel(emptyMap()) }
            .flowOn(Dispatchers.IO)

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

    override suspend fun addAnalyzedPerson(imageName: String, imageBytes: ByteArray) {
        personDao.insertPersonAndFace(imageName, imageBytes)
    }

    override suspend fun fetchAndSyncPhotoCount(localModels: List<PersonModel>): List<PersonModel> =
        runSuspendCatching {
            val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
            val response = webService.getPersonFrequency(
                PersonFrequencyRequest(dbName, localModels.map { it.inputName }),
            )

            localModels.map { personModel ->
                val matchedFrequency = response.frequencies.find { it.personName == personModel.inputName }
                personModel.copy(photoCount = matchedFrequency?.frequency ?: 0)
            }
        }.map { updatedModels ->
            normalizeScores(updatedModels)
        }.getOrElse { e ->
            Log.e(tag, "사진 개수 동기화 오류", e)
            normalizeScores(localModels)
        }

    override suspend fun getMismatchedFaceNames(): List<Pair<String, String>> =
        personDao.getMismatchedFaceNames().map {
            it.serverName to it.actualName
        }

    override suspend fun deleteAnalyzedPerson(person: PersonModel): Result<Unit> = withContext(ioDispatcher) {
        runSuspendCatching {
            val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

            coroutineScope {
                val webDelete = async {
                    webService.deleteEntity(DeleteEntityRequest(dbName, person.inputName))
                }
                val aiDelete = async {
                    aiService.deletePerson(
                        dbName.toRequestBody("text/plain".toMediaTypeOrNull()),
                        person.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    )
                }

                awaitAll(webDelete, aiDelete)
            }

            personDao.deletePersonById(person.id)
        }
    }

    override suspend fun getPersonPhotoNames(personName: String): Result<List<String>> = runSuspendCatching {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        webService.sendPersonData(
            PersonSearchRequest(
                dbName = dbName,
                personName = personName,
            ),
        )
    }

    override suspend fun isNameExists(inputName: String): Boolean = personDao.isNameExists(inputName)

    override suspend fun updatePersonFullInfo(
        personId: Long,
        newName: String,
        newPhone: String,
        isHome: Boolean,
        faceId: Long?,
    ): Result<Long> = withContext(ioDispatcher) {
        runSuspendCatching {
            val currentPersonEntity = personDao.getPersonById(personId)
            val oldName = currentPersonEntity?.inputName ?: ""
            val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
            val targetPersonId = personDao.getPersonIdByName(newName)

            if (targetPersonId != null && targetPersonId != personId) {
                personDao.mergePersons(personId, targetPersonId)
                webService.changePersonName(ChangeNameRequest(dbName, oldName, newName))
                targetPersonId
            } else {
                personDao.updatePersonFullInfo(personId, newName, newPhone, isHome, faceId)

                if (oldName != newName && oldName.isNotEmpty()) {
                    webService.changePersonName(ChangeNameRequest(dbName, oldName, newName))
                }
                personId
            }
        }
    }

    override suspend fun updateRepresentativeFace(personId: Long, faceId: Long): Result<Unit> = runSuspendCatching {
        personDao.updateRepresentativeFace(personId, faceId)
    }

    override suspend fun changePersonNameOnServer(oldName: String, newName: String): Result<Unit> = withContext(ioDispatcher) {
        runSuspendCatching {
            val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

            webService.changePersonName(ChangeNameRequest(dbName, oldName, newName))
            Unit
        }
    }
}
