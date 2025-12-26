package com.example.metasearch.core.room.api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.metasearch.core.room.api.entity.FaceEntity
import com.example.metasearch.core.room.api.entity.PersonEntity
import com.example.metasearch.core.room.api.relations.PersonWithFaces
import kotlinx.coroutines.flow.Flow

data class NamePair(
    val name: String,
    val inputName: String,
)

@Dao
interface PersonDao {
    @Insert
    suspend fun insertPerson(
        person: PersonEntity,
    ): Long

    @Insert
    suspend fun insertFace(
        face: FaceEntity,
    )

    @Transaction
    @Query("SELECT * FROM persons")
    fun getPersonsWithFaces(): Flow<List<PersonWithFaces>>

    @Transaction
    @Query("SELECT * FROM persons ORDER BY input_name ASC")
    fun getAllPersonsWithFaces(): Flow<List<PersonWithFaces>>

    @Transaction
    @Query("SELECT * FROM persons WHERE input_name = :inputName LIMIT 1")
    suspend fun getPersonWithFacesByInputName(
        inputName: String,
    ): PersonWithFaces?

    @Transaction
    @Query("SELECT * FROM persons WHERE id = :personId")
    suspend fun getPersonWithFacesById(
        personId: Long,
    ): PersonWithFaces?

    @Query(
        """
        UPDATE persons
        SET input_name = :newName,
            phone_number = :newPhoneNumber,
            is_home_display = :homeDisplay
        WHERE input_name = :oldName
        """,
    )
    suspend fun updatePersonInfoByName(
        oldName: String,
        newName: String,
        newPhoneNumber: String,
        homeDisplay: Boolean,
    ): Int

    @Query("SELECT name, input_name AS inputName FROM persons WHERE name != input_name")
    suspend fun getMismatchedNames(): List<NamePair>

    @Transaction
    suspend fun insertPersonAndFace(
        imageName: String,
        imageBytes: ByteArray,
    ) {
        val newPersonEntity = PersonEntity(
            name = imageName,
            inputName = imageName,
            phoneNumber = "",
            isHomeDisplay = false,
        )
        val personId = insertPerson(newPersonEntity)

        val newFaceEntity = FaceEntity(
            personId = personId,
            imageName = imageName,
            imageData = imageBytes,
            phoneNumber = "",
        )
        insertFace(newFaceEntity)
    }

    @Query("SELECT COUNT(*) FROM persons")
    suspend fun getPersonCount(): Int

    @Query("SELECT * FROM persons WHERE id = :personId")
    suspend fun getPersonById(
        personId: Long,
    ): PersonEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM persons WHERE input_name = :inputName LIMIT 1)")
    suspend fun isNameExists(
        inputName: String,
    ): Boolean

    @Query(
        """
        SELECT T1.input_name FROM persons AS T1
        JOIN faces AS T2 ON T1.id = T2.person_id
        WHERE T2.image_name = :imageName LIMIT 1
        """,
    )
    suspend fun getInputNameByImageName(
        imageName: String,
    ): String?

    @Query(
        """
        UPDATE faces
        SET thumbnail_data = :thumbnailData
        WHERE person_id IN (SELECT id FROM persons WHERE input_name = :inputName)
        """,
    )
    suspend fun updateFaceThumbnailsByPersonName(
        inputName: String,
        thumbnailData: ByteArray,
    ): Int

    @Query("DELETE FROM persons WHERE input_name = :inputName")
    suspend fun deletePersonByInputName(
        inputName: String,
    )

    @Query("DELETE FROM persons WHERE id = :personId")
    suspend fun deletePersonById(personId: Long)
}
