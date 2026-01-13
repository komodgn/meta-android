package com.metasearch.android.core.data.impl.mapper

import com.metasearch.android.core.model.FaceModel
import com.metasearch.android.core.model.PersonModel
import com.metasearch.android.core.model.PhotoGroup
import com.metasearch.android.core.model.SearchResult
import com.metasearch.android.core.network.response.PhotoNameResponse
import com.metasearch.android.core.network.response.PhotoResponse
import com.metasearch.android.core.room.api.entity.FaceEntity
import com.metasearch.android.core.room.api.entity.PersonEntity
import com.metasearch.android.core.room.api.relations.PersonWithFaces
import kotlin.collections.map

internal fun PhotoResponse.toModel(): SearchResult {
    val commonList = photos.commonPhotos.distinct()
    val individualMap = photos.individualPhotos
    val resultGroups = mutableListOf<PhotoGroup>()

    if (commonList.isNotEmpty()) {
        val relatedCategories = individualMap.filter { entry ->
            entry.value.any { it in commonList }
        }.keys.joinToString(", ")

        resultGroups.add(PhotoGroup(categoryName = relatedCategories, photoNames = commonList))
    }

    individualMap.forEach { (category, photoNames) ->
        val filteredNames = photoNames.filter { it !in commonList }

        if (filteredNames.isNotEmpty()) {
            resultGroups.add(
                PhotoGroup(
                    categoryName = category,
                    photoNames = filteredNames,
                ),
            )
        }
    }

    return SearchResult(groups = resultGroups)
}

internal fun PhotoNameResponse.toModel(): List<String> {
    return this.photoNames
}

internal fun PersonWithFaces.toModel(callDurations: Map<String, Long>): PersonModel {
    val totalDuration = callDurations[person.phoneNumber] ?: 0L

    return PersonModel(
        id = person.id,
        representativeFaceId = person.representativeFaceId,
        name = person.name,
        inputName = person.inputName,
        phoneNumber = person.phoneNumber,
        isHomeDisplay = person.isHomeDisplay,
        totalDuration = totalDuration,
        faces = faces.map { faceEntity ->
            FaceModel(
                id = faceEntity.id,
                personId = faceEntity.personId,
                imageName = faceEntity.imageName,
                imageData = faceEntity.imageData,
            )
        },
    )
}

internal fun PersonEntity.toModel(faces: List<FaceEntity>) = PersonModel(
    id = id,
    name = name,
    inputName = inputName,
    phoneNumber = phoneNumber,
    isHomeDisplay = isHomeDisplay,
    representativeFaceId = representativeFaceId,
    faces = faces.map { it.toModel() },
)

internal fun FaceEntity.toModel() = FaceModel(
    id = id,
    personId = personId,
    imageName = imageName,
    imageData = imageData,
)
