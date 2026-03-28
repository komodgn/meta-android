package com.metasearch.android.core.testing.repository

import android.content.Context
import com.metasearch.android.core.data.api.repository.ImageAnalysisRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

@Inject
public class FakeImageAnalisisRepository : ImageAnalysisRepository {

    public sealed class Status {
        public data object Success : Status()
        public data object Empty : Status()
        public data object Error : Status()
    }

    private var status: Status = Status.Success

    public fun setup(status: Status) {
        this.status = status
    }

    override fun getAnalysisStatus(context: Context): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun runFullAnalysis() {
        TODO("Not yet implemented")
    }

    override suspend fun getImageDescription(uriString: String): Result<String?> {
        TODO("Not yet implemented")
    }
}
