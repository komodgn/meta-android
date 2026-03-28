package com.metasearch.android.core.testing.repository

import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.core.model.Person
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

@Inject
public class FakePersonRepository : PersonRepository {

    public sealed class Status {
        public data object Success : Status()
        public data object Empty : Status()
        public data object Error : Status()
    }

    private var status: Status = Status.Success

    public fun setup(status: Status) {
        this.status = status
    }

    override fun getAllPersons(): Flow<List<Person>> {
        TODO("Not yet implemented")
    }

    override fun getHomeDisplayPersons(): Flow<List<Person>> {
        TODO("Not yet implemented")
    }

    override fun getPersonById(personId: Long): Flow<Person?> {
        TODO("Not yet implemented")
    }

    override suspend fun getInputNameBySystemName(systemName: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getPersonCount(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getPersonIdByImageName(imageName: String): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun addFaceToExistingPerson(personId: Long, imageName: String, imageBytes: ByteArray): Long {
        TODO("Not yet implemented")
    }

    override suspend fun addAnalyzedPerson(imageName: String, imageBytes: ByteArray) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAndSyncPhotoCount(localModels: List<Person>): List<Person> {
        TODO("Not yet implemented")
    }

    override suspend fun getMismatchedFaceNames(): List<Pair<String, String>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAnalyzedPerson(person: Person): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getPersonPhotoNames(personName: String): Result<List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun isNameExists(inputName: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updatePersonFullInfo(
        personId: Long,
        newName: String,
        newPhone: String,
        isHome: Boolean,
        faceId: Long?,
    ): Result<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun updateRepresentativeFace(personId: Long, faceId: Long): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun changePersonNameOnServer(oldName: String, newName: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}
