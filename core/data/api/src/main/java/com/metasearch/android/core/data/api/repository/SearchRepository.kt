package com.metasearch.android.core.data.api.repository

import com.metasearch.android.core.model.Circle
import com.metasearch.android.core.model.DragSearchResult
import com.metasearch.android.core.model.NLSearchResult
import java.io.File

interface SearchRepository {
    suspend fun focusingSearch(
        imageFile: File,
        circles: List<Circle>,
    ): Result<DragSearchResult>

    /**
     * Analyzes natural language queries and searches for corresponding photos using a cache.
     *
     * @param query The user's search query in natural language.
     * @return A [Result] containing the [NLSearchResult].
     */
    suspend fun nlSearch(
        query: String,
    ): Result<NLSearchResult>

    /**
     * Clears all cached entities used for natural language searching.
     */
    fun clearEntityCache()
}
