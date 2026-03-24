package com.metasearch.android.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.metasearch.android.core.di.ChildWorkerFactory
import dev.zacsweers.metro.Provider
import kotlin.reflect.KClass

class MetroWorkerFactory(
    private val workerFactories: Map<KClass<out ListenableWorker>, Provider<ChildWorkerFactory>>,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        val workerClass = runCatching {
            Class.forName(workerClassName, false, appContext.classLoader)
                .asSubclass(ListenableWorker::class.java)
                .kotlin
        }.getOrElse { return null }

        val factoryProvider = workerFactories[workerClass] ?: return null
        return factoryProvider.invoke().create(appContext, workerParameters)
    }
}
