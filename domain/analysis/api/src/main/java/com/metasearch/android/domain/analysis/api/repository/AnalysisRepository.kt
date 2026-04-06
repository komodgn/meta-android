package com.metasearch.android.domain.analysis.api.repository

import com.metasearch.android.data.domain.AnalysisResult

interface AnalysisRepository {
    suspend fun getAlreadyAnalyzedPaths(): List<String>
    suspend fun getFileNameByPath(path: String): String?
    suspend fun saveAnalyzedPath(path: String, fileName: String): Result<Unit>
    suspend fun uploadImages(uriStrings: List<String>, dbName: String): List<Pair<String, String>>
    suspend fun deleteLocalPath(path: String): Result<Unit>
    suspend fun deleteImage(fileName: String, dbName: String): Result<Unit>
    suspend fun finishAnalysis(dbName: String, lastIndex: Int): Result<AnalysisResult>
    suspend fun getImageTripleData(photoName: String): Result<String>
    suspend fun getAiCompletion(prompt: String): Result<String>
}
