package com.metasearch.android.domain.analysis.api.usecase

import com.metasearch.android.data.domain.AnalysisResult
import com.metasearch.android.data.domain.UploadedImage

interface ProcessAnalysisResultUseCase {
    suspend operator fun invoke(
        dbName: String,
        successfulPaths: List<UploadedImage>,
    ): Result<AnalysisResult>
}
