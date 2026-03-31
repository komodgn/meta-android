package com.metasearch.android.impl.usecase

import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkManager
import com.metasearch.android.api.model.WorkOptions
import com.metasearch.android.api.usecase.WorkScheduleUseCase
import dev.zacsweers.metro.Inject
import kotlin.reflect.KClass

@Inject
class WorkScheduleUseCase(
    private val workManager: WorkManager,
) : WorkScheduleUseCase {

    override fun <W : CoroutineWorker> scheduleNow(
        workName: String,
        klass: KClass<W>,
        options: WorkOptions,
        params: Data.Builder.() -> Data.Builder,
    ) {
        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(options.networkRequirement)
            .setRequiresCharging(options.requiresCharging)
            .build()

        val workRequest = androidx.work.OneTimeWorkRequest.Builder(klass.java as Class<out androidx.work.ListenableWorker>)
            .setConstraints(constraints)
            .setInputData(params(Data.Builder()).build())
            .build()

        workManager.enqueueUniqueWork(
            workName,
            options.existingWorkPolicy,
            workRequest,
        )
    }

    override fun cancelUniqueWork(workName: String) {
        workManager.cancelUniqueWork(workName)
    }

    override fun cancelAllScheduledWork() {
        workManager.cancelAllWork()
    }
}
