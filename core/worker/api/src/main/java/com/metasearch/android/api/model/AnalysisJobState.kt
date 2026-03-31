package com.metasearch.android.api.model

import androidx.work.WorkInfo

data class AnalysisJobState(
    val state: WorkInfo.State,
    val progress: Int = 0,
    val message: String? = null,
)
