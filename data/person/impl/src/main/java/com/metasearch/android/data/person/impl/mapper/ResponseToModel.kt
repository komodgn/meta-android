package com.metasearch.android.data.person.impl.mapper

import com.metasearch.android.core.network.response.PersonFrequencyResponse
import com.metasearch.android.core.room.api.entity.FaceEntity
import com.metasearch.android.core.room.api.entity.PersonEntity
import com.metasearch.android.core.room.api.relations.PersonWithFaces
import com.metasearch.android.data.domain.Face
import com.metasearch.android.data.domain.Person
import com.metasearch.android.data.domain.PersonFrequency
import kotlin.collections.map

internal fun PersonWithFaces.toModel(callDurations: Map<String, Long>): Person {
    val totalDuration = callDurations[person.phoneNumber] ?: 0L

    return Person(
        id = person.id,
        representativeFaceId = person.representativeFaceId,
        name = person.name,
        inputName = person.inputName,
        phoneNumber = person.phoneNumber,
        isHomeDisplay = person.isHomeDisplay,
        totalDuration = totalDuration,
        faces = faces.map { faceEntity ->
            Face(
                id = faceEntity.id,
                personId = faceEntity.personId,
                imageName = faceEntity.imageName,
                imageData = faceEntity.imageData,
            )
        },
    )
}

internal fun PersonEntity.toModel(faces: List<FaceEntity>) = Person(
    id = id,
    name = name,
    inputName = inputName,
    phoneNumber = phoneNumber,
    isHomeDisplay = isHomeDisplay,
    representativeFaceId = representativeFaceId,
    faces = faces.map { it.toModel() },
)

internal fun FaceEntity.toModel() = Face(
    id = id,
    personId = personId,
    imageName = imageName,
    imageData = imageData,
)

internal fun PersonFrequencyResponse.toModel(): List<PersonFrequency> {
    return this.frequencies.map {
        PersonFrequency(
            personName = it.personName,
            frequency = it.frequency,
        )
    }
}
