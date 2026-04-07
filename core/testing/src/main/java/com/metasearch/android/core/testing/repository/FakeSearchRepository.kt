package com.metasearch.android.core.testing.repository

import com.metasearch.android.data.domain.Circle
import com.metasearch.android.data.domain.DragSearchResult
import com.metasearch.android.domain.search.api.repository.SearchRepository
import dev.zacsweers.metro.Inject
import java.io.File

@Inject
public class FakeSearchRepository : SearchRepository {

    public sealed class Status {
        public data object Success : Status()
        public data object Empty : Status()
        public data object Error : Status()
    }

    private var status: Status = Status.Success

    public fun setup(status: Status) {
        this.status = status
    }

    override suspend fun analyzeFocusingImage(
        dbName: String,
        imageFile: File,
        circles: List<Circle>,
    ): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun searchPhotosByKeywords(
        dbName: String,
        keywords: List<String>,
    ): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun extractKeywordsFromNL(query: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun findPhotosByDetectedObjects(
        dbName: String,
        properties: List<String>,
    ): DragSearchResult {
        TODO("Not yet implemented")
    }
}
