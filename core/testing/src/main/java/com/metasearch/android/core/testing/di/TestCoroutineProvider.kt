package com.metasearch.android.core.testing.di

import com.metasearch.android.core.data.impl.annotation.IoDispatcher
import com.metasearch.android.core.data.impl.di.CoroutineGraph
import com.metasearch.android.core.di.scope.DataScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

@ContributesTo(DataScope::class, replaces = [CoroutineGraph::class])
interface TestCoroutineProvider {

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(
        testDispatcher: TestDispatcher,
    ): CoroutineDispatcher = testDispatcher

    @SingleIn(DataScope::class)
    @Provides
    fun provideTestDispatcher(): TestDispatcher = StandardTestDispatcher()
}
