package com.metasearch.android.core.data.api.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface ImageAnalysisRepository {
    fun getAnalysisStatus(context: Context): Flow<Boolean>

    suspend fun runFullAnalysis()
    suspend fun getImageDescription(uriString: String): Result<String?>
}
