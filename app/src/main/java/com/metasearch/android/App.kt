package com.metasearch.android

import android.app.Application
import androidx.work.Configuration
import com.metasearch.android.di.AndroidAppGraph
import com.metasearch.android.di.MetroWorkerFactory
import dev.zacsweers.metro.createGraphFactory
import kotlin.getValue

class App : Application(), Configuration.Provider {
    val appGraph by lazy { createGraphFactory<AndroidAppGraph.Factory>().create(this) }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(MetroWorkerFactory(appGraph.workerFactories))
            .build()
}
