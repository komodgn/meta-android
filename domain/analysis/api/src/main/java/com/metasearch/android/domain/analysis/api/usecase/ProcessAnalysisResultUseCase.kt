package com.metasearch.android.domain.analysis.api.usecase

import com.metasearch.android.data.domain.AnalysisResult

interface ProcessAnalysisResultUseCase {
    suspend operator fun invoke(
        dbName: String,
        successfulPaths: List<Pair<String, String>>,
    ): Result<AnalysisResult>
}
