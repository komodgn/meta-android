package com.example.metasearch.core.data.api.repository

import com.example.metasearch.core.model.PersonModel
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun getAllPersons(): Flow<List<PersonModel>>

    fun getHomeDisplayPersons(): Flow<List<PersonModel>>

    /**
     * ID로 단일 인물 정보 조회
     */
    fun getPersonById(personId: Long): Flow<PersonModel?>

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

    /**
     * 분석된 인물 삭제
     */
    suspend fun deleteAnalyzedPerson(person: PersonModel): Result<Unit>

    /**
     * @return 해당 인물이 포함된 사진 파일명 리스트
     */
    suspend fun getPersonPhotoNames(personName: String): Result<List<String>>

    suspend fun isNameExists(inputName: String): Boolean

    suspend fun updatePersonFullInfo(
        personId: Long,
        newName: String,
        newPhone: String,
        isHome: Boolean,
        faceId: Long?,
    ): Result<Unit>

    suspend fun changePersonNameOnServer(oldName: String, newName: String): Result<Unit>
}
