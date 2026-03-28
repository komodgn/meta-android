package com.metasearch.android.core.data.impl.di

import com.metasearch.android.core.data.impl.annotation.IoDispatcher
import com.metasearch.android.core.di.scope.DataScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@ContributesTo(DataScope::class)
interface CoroutineGraph {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
