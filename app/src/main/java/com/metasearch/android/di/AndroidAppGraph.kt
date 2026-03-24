package com.metasearch.android.di

import android.app.Activity
import android.content.Context
import androidx.work.ListenableWorker
import com.metasearch.android.core.di.ChildWorkerFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import kotlin.reflect.KClass

@DependencyGraph(AppScope::class)
interface AndroidAppGraph {

    @Multibinds
    val activityProviders: Map<KClass<out Activity>, Provider<Activity>>

    @Multibinds
    val workerFactories: Map<KClass<out ListenableWorker>, Provider<ChildWorkerFactory>>

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides applicationContext: Context,
        ): AndroidAppGraph
    }
}
