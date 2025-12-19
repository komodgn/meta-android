package com.example.metasearch.core.data.api.repository

import com.example.metasearch.core.model.CircleModel
import com.example.metasearch.core.model.SearchResult
import java.io.File

interface SearchRepository {
    suspend fun focusingSearch(
        imageFile: File,
        circles: List<CircleModel>,
    ): Result<SearchResult>
}
