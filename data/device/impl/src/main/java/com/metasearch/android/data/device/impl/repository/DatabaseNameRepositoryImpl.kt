package com.metasearch.android.data.device.impl.repository

import com.metasearch.android.core.datastore.api.datasource.DeviceIdDataSource
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.device.api.repository.DatabaseNameRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.util.UUID

@SingleIn(DataScope::class)
@Inject
class DatabaseNameRepositoryImpl(
    private val dataSource: DeviceIdDataSource,
) : DatabaseNameRepository {

    override suspend fun getPersistentDeviceDatabaseName(): String {
        var uniqueId = dataSource.getDeviceId()

        if (uniqueId.isBlank()) {
            uniqueId = UUID.randomUUID().toString().replace("-", "")
            dataSource.setDeviceId(uniqueId)
        }

        return "db$uniqueId"
    }
}
