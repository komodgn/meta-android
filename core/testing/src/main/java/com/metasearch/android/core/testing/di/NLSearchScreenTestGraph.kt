package com.metasearch.android.core.testing.di

import com.metasearch.android.core.testing.robot.search.NLSearchScreenRobot
import com.metasearch.android.feature.screens.context.NLSearchScreenContext
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides

interface NLSearchScreenTestGraph {
    val nLSearchScreenRobotProvider: Provider<NLSearchScreenRobot>

    @Provides
    fun provideNLSearchScreenContext(
        factory: NLSearchScreenContext.Factory,
    ): NLSearchScreenContext {
        return factory.createNLSearchScreenContext()
    }
}

fun createSearchScreenTestGraph(): NLSearchScreenTestGraph = createTestAppGraph()
