package com.metasearch.android.core.worker.api.usecase

import androidx.work.CoroutineWorker
import androidx.work.Data
import com.metasearch.android.core.worker.api.model.WorkOptions
import kotlin.reflect.KClass

interface WorkScheduleUseCase {

    /**
     * @param W Worker class to run
     */
    fun <W : CoroutineWorker> scheduleNow(
        workName: String,
        klass: KClass<W>,
        options: WorkOptions = WorkOptions(),
        params: Data.Builder.() -> Data.Builder = { this },
    )

    fun cancelUniqueWork(workName: String)

    fun cancelAllScheduledWork()
}
