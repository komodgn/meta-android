package com.metasearch.android.core.room.api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.metasearch.android.core.room.api.entity.FaceEntity
import com.metasearch.android.core.room.api.entity.PersonEntity
import com.metasearch.android.core.room.api.relations.PersonWithFaces
import kotlinx.coroutines.flow.Flow

data class NameMapping(
    val serverName: String,
    val actualName: String,
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
    ): Long

    /**
     * 서버 응답(imageName)을 받았을 때 현재 누구에게 소속시켜야 할지 찾는 쿼리
     */
    @Query(
        """
            SELECT person_id FROM faces
            WHERE image_name = :serverLabel
            LIMIT 1
        """,
    )
    suspend fun findCurrentPersonIdByServerLabel(serverLabel: String): Long?

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
    fun getPersonWithFacesFlow(personId: Long): Flow<PersonWithFaces?>

    @Transaction
    suspend fun mergePersons(sourceId: Long, targetId: Long) {
        updateFacesPersonId(sourceId, targetId)

        deletePersonById(sourceId)
    }

    @Query("UPDATE faces SET person_id = :targetId WHERE person_id = :sourceId")
    suspend fun updateFacesPersonId(sourceId: Long, targetId: Long)

    @Query("UPDATE persons SET phone_number = :phone, is_home_display = :isHome WHERE id = :personId")
    suspend fun updatePersonBasicInfo(personId: Long, phone: String, isHome: Boolean)

    @Query("SELECT id FROM persons WHERE input_name = :name LIMIT 1")
    suspend fun getPersonIdByName(name: String): Long?

    @Query(
        """
        SELECT person_id
        FROM faces
        WHERE image_name = :imageName
        LIMIT 1
        """,
    )
    suspend fun getPersonIdByImageName(imageName: String): Long?

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

    @Query(
        """
        UPDATE persons
        SET input_name = :newName,
            phone_number = :newPhone,
            is_home_display = :isHome,
            representative_face_id = :faceId
        WHERE id = :personId
        """,
    )
    suspend fun updatePersonFullInfo(
        personId: Long,
        newName: String,
        newPhone: String,
        isHome: Boolean,
        faceId: Long?,
    ): Int

    @Query(
        """
        SELECT DISTINCT f.image_name AS serverName, p.input_name AS actualName
        FROM faces f
        JOIN persons p ON f.person_id = p.id
        WHERE f.image_name != p.input_name
        AND f.image_name LIKE '인물%'
        """,
    )
    suspend fun getMismatchedFaceNames(): List<NameMapping>

    @Transaction
    suspend fun insertPersonAndFace(
        imageName: String,
        imageBytes: ByteArray,
    ) {
        val personId = insertPerson(PersonEntity(name = imageName, inputName = imageName))
        val faceId = insertFace(
            FaceEntity(
                personId = personId,
                imageName = imageName,
                imageData = imageBytes,
            ),
        )

        updateRepresentativeFace(personId, faceId)
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

    /**
     * 이전에 분석된 적 있는 imageName인지 확인하여 현재 주인(person_id)을 반환
     */
    @Query("SELECT person_id FROM faces WHERE image_name = :imageName LIMIT 1")
    suspend fun findPersonIdByImageName(imageName: String): Long?

    @Query("UPDATE persons SET representative_face_id = :faceId WHERE id = :personId")
    suspend fun updateRepresentativeFace(personId: Long, faceId: Long)

    @Query("DELETE FROM persons WHERE input_name = :inputName")
    suspend fun deletePersonByInputName(
        inputName: String,
    )

    @Query("DELETE FROM persons WHERE id = :personId")
    suspend fun deletePersonById(personId: Long)
}
