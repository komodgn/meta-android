package com.metasearch.android.core.testing.repository

import com.metasearch.android.core.data.api.repository.SearchRepository
import com.metasearch.android.core.model.Circle
import com.metasearch.android.core.model.DragSearchResult
import com.metasearch.android.core.model.NLSearchResult
import com.metasearch.android.core.model.PhotoGroup
import com.metasearch.android.core.model.fakes
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

    override suspend fun focusingSearch(
        imageFile: File,
        circles: List<Circle>,
    ): Result<DragSearchResult> = when (status) {
        is Status.Success -> Result.success(DragSearchResult(groups = PhotoGroup.fakes()))
        is Status.Empty -> Result.success(DragSearchResult(groups = emptyList()))
        is Status.Error -> Result.failure(Exception("Fake Search Error"))
    }

    override suspend fun nlSearch(query: String): Result<NLSearchResult> = when (status) {
        is Status.Success -> Result.success(NLSearchResult(matchedUris = listOf("uri1", "uri2")))
        is Status.Empty -> Result.success(NLSearchResult(emptyList()))
        is Status.Error -> Result.failure(Exception("Fake NL Error"))
    }

    override fun clearEntityCache() { /* Empty block */ }
}
