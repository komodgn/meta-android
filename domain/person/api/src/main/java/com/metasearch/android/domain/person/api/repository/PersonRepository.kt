package com.metasearch.android.domain.person.api.repository

import com.metasearch.android.data.domain.Person
import com.metasearch.android.data.domain.PersonFrequency
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun getAllPersons(): Flow<List<Person>>

    fun getPersonById(personId: Long): Flow<Person?>

    suspend fun getInputNameBySystemName(systemName: String): String?

    suspend fun getPersonCount(): Int

    suspend fun getPersonIdByImageName(imageName: String): Long?

    suspend fun addFaceToExistingPerson(personId: Long, imageName: String, imageBytes: ByteArray): Long

    suspend fun addAnalyzedPerson(imageName: String, imageBytes: ByteArray)

    suspend fun getMismatchedFaceNames(): List<Pair<String, String>>

    suspend fun isNameExists(inputName: String): Boolean

    suspend fun updateRepresentativeFace(
        personId: Long,
        faceId: Long,
    ): Result<Unit>

    suspend fun updatePersonInfo(
        personId: Long,
        newName: String,
        newPhone: String,
        isHome: Boolean,
        faceId: Long?,
    ): Result<Long>

    suspend fun changePersonNameOnServer(oldName: String, newName: String): Result<Unit>

    suspend fun getPersonPhotoNames(personName: String): Result<List<String>>

    suspend fun fetchPhotoFrequencies(names: List<String>): Result<List<PersonFrequency>>

    suspend fun deleteFromWebService(inputName: String): Result<Unit>

    suspend fun deleteFromAiService(personName: String): Result<Unit>

    suspend fun deleteFromLocalDb(personId: Long): Result<Unit>
}
