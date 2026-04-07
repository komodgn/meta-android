package com.metasearch.android.data.device.impl.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.device.impl.repository.DatabaseNameRepositoryImpl
import com.metasearch.android.domain.device.api.repository.DatabaseNameRepository
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
interface DeviceDataGraph {
    @Binds
    val DatabaseNameRepositoryImpl.bind: DatabaseNameRepository
}
