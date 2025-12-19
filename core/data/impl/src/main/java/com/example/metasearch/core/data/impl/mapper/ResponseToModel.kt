package com.example.metasearch.core.data.impl.mapper

import android.content.Context
import com.example.metasearch.core.data.impl.util.GalleryImageManager
import com.example.metasearch.core.model.NLSearchResult
import com.example.metasearch.core.model.PhotoGroup
import com.example.metasearch.core.model.SearchResult
import com.example.metasearch.core.network.response.PhotoNameResponse
import com.example.metasearch.core.network.response.PhotoResponse

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
                )
            )
        }
    }

    return SearchResult(groups = resultGroups)
}

internal fun PhotoNameResponse.toModel(context: Context): NLSearchResult {
    val matchedUris = GalleryImageManager.findMatchedUris(
        photoNamesFromServer = this.photoNames,
        context = context,
    ).map { it.toString() }

    return NLSearchResult(matchedUris = matchedUris)
}
