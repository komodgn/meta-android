package com.metasearch.android.core.data.impl.repository

import com.metasearch.android.core.data.api.repository.DatabaseNameRepository
import com.metasearch.android.core.datastore.api.datasource.DeviceIdDataSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.util.UUID

@SingleIn(AppScope::class)
class DatabaseNameRepositoryImpl @Inject constructor(
    private val deviceIdDataSource: DeviceIdDataSource,
) : DatabaseNameRepository {
    override suspend fun getPersistentDeviceDatabaseName(): String {
        var uniqueId = deviceIdDataSource.getDeviceId()

        if (uniqueId.isBlank()) {
            uniqueId = UUID.randomUUID().toString().replace("-", "")
            deviceIdDataSource.setDeviceId(uniqueId)
        }

        return "db$uniqueId"
    }
}
