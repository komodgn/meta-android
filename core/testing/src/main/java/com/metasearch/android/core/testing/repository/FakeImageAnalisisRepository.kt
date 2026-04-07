package com.metasearch.android.core.testing.repository

import com.metasearch.android.data.domain.AnalysisResult
import com.metasearch.android.data.domain.UploadedImage
import com.metasearch.android.domain.analysis.api.repository.AnalysisRepository
import dev.zacsweers.metro.Inject

@Inject
public class FakeImageAnalisisRepository : AnalysisRepository {

    public sealed class Status {
        public data object Success : Status()
        public data object Empty : Status()
        public data object Error : Status()
    }

    private var status: Status = Status.Success

    public fun setup(status: Status) {
        this.status = status
    }

    override suspend fun getAlreadyAnalyzedPaths(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getFileNameByPath(path: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveAnalyzedPath(path: String, fileName: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImages(
        uriStrings: List<String>,
        dbName: String,
    ): List<UploadedImage> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLocalPath(path: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteImage(fileName: String, dbName: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun finishAnalysis(
        dbName: String,
        lastIndex: Int,
    ): Result<AnalysisResult> {
        TODO("Not yet implemented")
    }

    override suspend fun getImageTripleData(photoName: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getAiCompletion(prompt: String): Result<String> {
        TODO("Not yet implemented")
    }
}
