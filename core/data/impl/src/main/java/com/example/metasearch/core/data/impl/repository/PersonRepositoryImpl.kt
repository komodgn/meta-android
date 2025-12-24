package com.example.metasearch.core.data.impl.repository

import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.util.Log
import com.example.metasearch.core.common.utils.normalizePhoneNumber
import com.example.metasearch.core.data.api.repository.DatabaseNameRepository
import com.example.metasearch.core.data.api.repository.PersonRepository
import com.example.metasearch.core.data.impl.mapper.toModel
import com.example.metasearch.core.model.PersonModel
import com.example.metasearch.core.network.request.PersonFrequencyRequest
import com.example.metasearch.core.network.service.WebService
import com.example.metasearch.core.room.api.dao.PersonDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val personDao: PersonDao,
    private val webService: WebService,
    private val databaseNameRepository: DatabaseNameRepository,
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

    override suspend fun getPersonCount(): Int = personDao.getPersonCount()

    override suspend fun addAnalyzedPerson(imageName: String, imageBytes: ByteArray) {
        if (!personDao.isNameExists(imageName)) {
            personDao.insertPersonAndFace(imageName, imageBytes)
        }
    }

    override suspend fun fetchAndSyncPhotoCount(localModels: List<PersonModel>): List<PersonModel> {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        return try {
            val response = webService.getPersonFrequency(
                PersonFrequencyRequest(
                    dbName = dbName,
                    personNames = localModels.map { it.inputName },
                ),
            )

            val updatedModels = localModels.map { personModel ->
                val matchedFrequency = response.frequencies.find {
                    it.personName == personModel.inputName
                }

                personModel.copy(
                    photoCount = matchedFrequency?.frequency ?: 0,
                )
            }

            normalizeScores(updatedModels)
        } catch (e: Exception) {
            Log.e(tag, "사진 개수 동기화 오류", e)
            normalizeScores(localModels)
        }
    }

    override suspend fun getMismatchedNames(): Map<String, String> {
        return personDao.getMismatchedNames().associate {
            it.name to it.inputName
        }
    }
}
