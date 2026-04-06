package com.metasearch.android.data.search.impl.mapper

import com.metasearch.android.core.network.response.PhotoNameResponse
import com.metasearch.android.core.network.response.PhotoResponse
import com.metasearch.android.data.domain.DragSearchResult
import com.metasearch.android.data.domain.PhotoGroup

internal fun PhotoResponse.toModel(): DragSearchResult {
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

    return DragSearchResult(groups = resultGroups)
}

internal fun PhotoNameResponse.toModel(): List<String> {
    return this.photoNames
}
