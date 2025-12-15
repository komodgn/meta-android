package com.example.metasearch.core.data.impl.repository

import com.example.metasearch.core.data.api.repository.DatabaseNameRepository
import com.example.metasearch.core.datastore.api.datasource.DeviceIdDataSource
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DatabaseNameRepositoryImpl @Inject constructor(
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
