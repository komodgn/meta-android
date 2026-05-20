package com.metasearch.android.core.worker.impl.usecase

import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.metasearch.android.core.worker.api.usecase.WorkerStatusUseCase
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Inject
class WorkerStatusUseCase(
    private val workManager: WorkManager,
) : WorkerStatusUseCase {

    override fun monitoringUniqueJobStatus(jobName: String): Flow<WorkInfo.State?> =
        monitorUniqueJob(jobName).map { it?.state }

    override fun monitorUniqueJob(jobName: String): Flow<WorkInfo?> = callbackFlow {
        val liveData = workManager.getWorkInfosForUniqueWorkLiveData(jobName)

        val observer = Observer<List<WorkInfo>> { infos ->
            trySend(infos.firstOrNull())
        }

        MainScope().launch {
            liveData.observeForever(observer)
        }

        awaitClose {
            MainScope().launch {
                liveData.removeObserver(observer)
            }
        }
    }
}
