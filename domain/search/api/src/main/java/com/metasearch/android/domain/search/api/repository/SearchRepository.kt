package com.metasearch.android.domain.search.api.repository

import com.metasearch.android.data.domain.Circle
import com.metasearch.android.data.domain.DragSearchResult
import java.io.File

interface SearchRepository {
    suspend fun analyzeFocusingImage(dbName: String, imageFile: File, circles: List<Circle>): List<String>
    suspend fun searchPhotosByKeywords(dbName: String, keywords: List<String>): List<String>
    suspend fun extractKeywordsFromNL(query: String): List<String>
    suspend fun findPhotosByDetectedObjects(dbName: String, properties: List<String>): DragSearchResult
}
