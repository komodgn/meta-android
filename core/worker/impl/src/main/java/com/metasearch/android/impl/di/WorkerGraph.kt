package com.metasearch.android.impl.di

import android.content.Context
import androidx.work.WorkManager
import com.metasearch.android.core.di.scope.WorkerScope
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
        impl: com.metasearch.android.impl.usecase.WorkScheduleUseCase,
    ): com.metasearch.android.api.usecase.WorkScheduleUseCase

    @Binds
    fun bindWorkerStatusUseCase(
        impl: com.metasearch.android.impl.usecase.WorkerStatusUseCase,
    ): com.metasearch.android.api.usecase.WorkerStatusUseCase
}
