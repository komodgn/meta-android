package com.example.metasearch.core.room.impl

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.metasearch.core.room.api.dao.PersonDao
import com.example.metasearch.core.room.api.entity.FaceEntity
import com.example.metasearch.core.room.api.entity.PersonEntity
import com.example.metasearch.core.room.impl.database.AppDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.java

private val TEST_PERSON_ENTITY = PersonEntity(
    inputName = "홍길동",
    phoneNumber = "010-1234-5678",
    isHomeDisplay = true,
)

private val TEST_FACE_ENTITY = FaceEntity(
    personId = 1L,
    imageName = "face_1.jpg",
    imageData = byteArrayOf(0x01, 0x02, 0x03),
    thumbnailData = byteArrayOf(0x04),
    phoneNumber = "",
)

private val TEST_FACE_ENTITY_2 = FaceEntity(
    personId = 1L,
    imageName = "face_2.jpg",
    imageData = byteArrayOf(0x05, 0x06),
    thumbnailData = byteArrayOf(0x07),
    phoneNumber = "",
)

@RunWith(AndroidJUnit4::class)
class PersonDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var personDao: PersonDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        )
            .allowMainThreadQueries() // 테스트에서는 메인 스레드 쿼리를 허용하여 runBlocking과 함께 사용
            .build()
        personDao = db.personDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndReadPerson() = runBlocking {
        val personId = personDao.insertPerson(TEST_PERSON_ENTITY)

        val faceEntity1 = TEST_FACE_ENTITY.copy(personId = personId)
        val faceEntity2 = TEST_FACE_ENTITY_2.copy(personId = personId)
        personDao.insertFace(faceEntity1)
        personDao.insertFace(faceEntity2)

        // PersonWithFaces 관계를 통해 데이터 조회
        val loadedPersonWithFaces = personDao.getPersonWithFacesById(personId)

        assertThat(loadedPersonWithFaces).isNotNull()
        assertThat(loadedPersonWithFaces?.person?.inputName).isEqualTo("홍길동")
        assertThat(loadedPersonWithFaces?.faces).hasSize(2)
        assertThat(loadedPersonWithFaces?.faces?.first()?.imageName).isEqualTo("face_1.jpg")
    }

    @Test
    fun deletePerson_shouldRemoveAllAssociatedFaces() = runBlocking {
        val personId = personDao.insertPerson(TEST_PERSON_ENTITY)
        val faceEntity1 = TEST_FACE_ENTITY.copy(personId = personId)
        personDao.insertFace(faceEntity1)

        // 삭제 전 확인
        assertThat(personDao.getPersonWithFacesById(personId)).isNotNull()

        // Person 삭제
        personDao.deletePersonByInputName(TEST_PERSON_ENTITY.inputName)

        // 삭제 후 검증
        assertThat(personDao.getPersonWithFacesById(personId)).isNull()
    }

    @Test
    fun updatePersonInfoByName_updatesAllRecords() = runBlocking {
        val personId1 = personDao.insertPerson(TEST_PERSON_ENTITY)
        val personId2 = personDao.insertPerson(TEST_PERSON_ENTITY.copy(id = 0, inputName = "이름2")) // 다른 레코드 삽입

        val rowsAffected = personDao.updatePersonInfoByName(
            oldName = "홍길동",
            newName = "김철수",
            newPhoneNumber = "010-9999-8888",
            homeDisplay = false,
        )

        // 1개 이상의 행이 업데이트되었는지 검증
        assertThat(rowsAffected).isGreaterThan(0)

        val updatedPerson = personDao.getPersonWithFacesByInputName("김철수")
        assertThat(updatedPerson?.person?.phoneNumber).isEqualTo("010-9999-8888")
        assertThat(updatedPerson?.person?.isHomeDisplay).isFalse()
        assertThat(personDao.getPersonWithFacesByInputName("홍길동")).isNull() // 이름이 바뀌었으므로 이전 이름으로는 조회되면 안됨
    }

    @Test
    fun getPersonsWithFaces_emitsFlow() = runBlocking {
        personDao.insertPerson(TEST_PERSON_ENTITY)

        // Flow 수집 및 검증
        val persons = personDao.getPersonsWithFaces().first()

        assertThat(persons).hasSize(1)
        assertThat(persons.first().person.inputName).isEqualTo("홍길동")
    }
}
