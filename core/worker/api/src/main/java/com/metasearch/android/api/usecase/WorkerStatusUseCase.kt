package com.metasearch.android.api.usecase

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface WorkerStatusUseCase {

    /**
     * Observe the current state (RUNNING, SUCCEEDED, etc.) of a specific task as a Flow.
     */
    fun monitoringUniqueJobStatus(jobName: String): Flow<WorkInfo.State?>

    /**
     * Observe the task details (including progress data) as a Flow.
     */
    fun monitorUniqueJob(jobName: String): Flow<WorkInfo?>
}
