package com.example.metasearch.core.data.api.repository

import com.example.metasearch.core.model.PersonModel
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun getAllPersons(): Flow<List<PersonModel>>

    fun getHomeDisplayPersons(): Flow<List<PersonModel>>

    /**
     * @return 분석 완료 후 저장된 인물 수
     */
    suspend fun getPersonCount(): Int

    /**
     * AI 분석 결과로 받은 인물 정보 저장
     */
    suspend fun addAnalyzedPerson(imageName: String, imageBytes: ByteArray)

    suspend fun fetchAndSyncPhotoCount(localModels: List<PersonModel>): List<PersonModel>

    suspend fun getMismatchedNames(): Map<String, String>
}
