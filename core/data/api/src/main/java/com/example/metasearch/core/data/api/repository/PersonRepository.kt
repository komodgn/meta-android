package com.example.metasearch.core.data.api.repository

import com.example.metasearch.core.model.PersonModel
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun getHomeDisplayPersons(): Flow<List<PersonModel>>

    suspend fun fetchAndSyncPhotoCount(localModels: List<PersonModel>): List<PersonModel>

    suspend fun getMismatchedNames(): Map<String, String>
}
