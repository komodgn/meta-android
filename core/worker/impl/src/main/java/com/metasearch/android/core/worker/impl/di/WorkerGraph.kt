package com.metasearch.android.core.worker.impl.di

import android.content.Context
import androidx.work.WorkManager
import com.metasearch.android.core.di.scope.WorkerScope
import com.metasearch.android.core.worker.api.usecase.WorkScheduleUseCase
import com.metasearch.android.core.worker.api.usecase.WorkerStatusUseCase
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(WorkerScope::class)
interface WorkerGraph {

    @Provides
    fun provideWorkManager(
        context: Context,
    ): WorkManager = WorkManager
        .getInstance(context)

    @Binds
    fun bindWorkScheduleUseCase(
        impl: com.metasearch.android.core.worker.impl.usecase.WorkScheduleUseCase,
    ): WorkScheduleUseCase

    @Binds
    fun bindWorkerStatusUseCase(
        impl: com.metasearch.android.core.worker.impl.usecase.WorkerStatusUseCase,
    ): WorkerStatusUseCase
}
