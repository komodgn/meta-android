package com.metasearch.android.data.remote.person

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.remote.person.request.ChangeNameRequest
import com.metasearch.android.data.remote.person.request.DeleteEntityRequest
import com.metasearch.android.data.remote.person.request.PersonFrequencyRequest
import com.metasearch.android.data.remote.person.request.PersonSearchRequest
import com.metasearch.android.data.remote.person.response.PersonFrequencyResponse
import com.metasearch.android.data.remote.person.service.PersonAIService
import com.metasearch.android.data.remote.person.service.PersonWebService
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@SingleIn(DataScope::class)
@Inject
class PersonClient(
    private val personAIService: PersonAIService,
    private val personWebService: PersonWebService,
) {
    suspend fun changePersonName(dbName: String, oldName: String, newName: String) {
        personWebService.changePersonName(ChangeNameRequest(dbName, oldName, newName))
    }

    suspend fun getPersonPhotoNames(dbName: String, personName: String): List<String> {
        return personWebService.sendPersonData(PersonSearchRequest(dbName, personName))
    }

    suspend fun getPersonFrequency(dbName: String, names: List<String>): PersonFrequencyResponse {
        return personWebService.getPersonFrequency(PersonFrequencyRequest(dbName, names))
    }

    suspend fun deleteFromWeb(dbName: String, inputName: String) {
        personWebService.deleteEntity(DeleteEntityRequest(dbName, inputName))
    }

    suspend fun deleteFromAi(dbName: String, personName: String) {
        val dbNamePart = dbName.toRequestBody("text/plain".toMediaTypeOrNull())
        val namePart = personName.toRequestBody("text/plain".toMediaTypeOrNull())
        personAIService.deletePerson(dbNamePart, namePart)
    }
}
