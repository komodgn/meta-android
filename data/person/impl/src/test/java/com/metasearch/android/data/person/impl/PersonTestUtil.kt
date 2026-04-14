package com.metasearch.android.data.person.impl

import com.metasearch.android.core.room.api.entity.PersonEntity
import com.metasearch.android.data.domain.Person

object PersonTestUtil {

    fun fakePersonEntity(
        id: Long = 1L,
        name: String = "인물1",
        inputName: String = "홍길동",
        phoneNumber: String = "010-1234-5678",
    ) = PersonEntity(
        id = id,
        name = name,
        inputName = inputName,
        phoneNumber = phoneNumber,
        isHomeDisplay = false,
        representativeFaceId = null,
    )

    fun fakePerson(
        id: Long = 1L,
        name: String = "인물1",
        inputName: String = "홍길동",
    ) = Person(
        id = id,
        name = name,
        inputName = inputName,
    )
}
