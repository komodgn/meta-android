package com.metasearch.android.data.file.impl.local.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.file.impl.local.repository.FileRepositoryImpl
import com.metasearch.android.domain.file.api.repository.FileRepository
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
interface FileDataGraph {
    @Binds
    val FileRepositoryImpl.bind: FileRepository
}
