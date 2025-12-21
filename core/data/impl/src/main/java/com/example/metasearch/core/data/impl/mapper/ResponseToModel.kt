package com.example.metasearch.core.data.impl.mapper

import com.example.metasearch.core.common.utils.normalizePhoneNumber
import com.example.metasearch.core.model.PersonModel
import com.example.metasearch.core.model.PhotoGroup
import com.example.metasearch.core.model.SearchResult
import com.example.metasearch.core.network.response.PhotoNameResponse
import com.example.metasearch.core.network.response.PhotoResponse
import com.example.metasearch.core.room.api.relations.PersonWithFaces

internal fun PhotoResponse.toModel(): SearchResult {
    val commonList = photos.commonPhotos.distinct()
    val individualMap = photos.individualPhotos
    val resultGroups = mutableListOf<PhotoGroup>()

    if (commonList.isNotEmpty()) {
        val relatedCategories = individualMap.filter { entry ->
            entry.value.any { it in commonList }
        }.keys.joinToString(" ") { "#$it" }

        resultGroups.add(PhotoGroup(categoryName = relatedCategories, photoNames = commonList))
    }

    individualMap.forEach { (category, photoNames) ->
        val filteredNames = photoNames.filter { it !in commonList }

        if (filteredNames.isNotEmpty()) {
            resultGroups.add(
                PhotoGroup(
                    categoryName = "#$category",
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
    val normalizedPhone = normalizePhoneNumber(this.person.phoneNumber)
    val totalDuration = callDurations.getOrDefault(normalizedPhone, 0L)

    return PersonModel(
        id = this.person.id.toInt(),
        imageName = this.faces.first().imageName,
        image = this.faces.first().imageData,
        inputName = this.person.inputName,
        phone = this.person.phoneNumber,
        homeDisplay = this.person.isHomeDisplay,
        photoCount = 0,
        totalDuration = totalDuration,
        normalizedScore = 0.0,
        thumbnailImage = this.faces.firstOrNull()?.thumbnailData,
    )
}
