package com.metasearch.android.core.data.api.repository

import com.metasearch.android.core.model.CircleModel
import com.metasearch.android.core.model.NLSearchResult
import com.metasearch.android.core.model.SearchResult
import java.io.File

interface SearchRepository {
    suspend fun focusingSearch(
        imageFile: File,
        circles: List<CircleModel>,
    ): Result<SearchResult>

    suspend fun nlSearch(
        query: String,
    ): Result<NLSearchResult>

    fun clearEntityCache()
}
