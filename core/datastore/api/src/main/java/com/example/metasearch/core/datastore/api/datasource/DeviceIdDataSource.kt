package com.example.metasearch.core.datastore.api.datasource

import kotlinx.coroutines.flow.Flow

interface DeviceIdDataSource {
    val deviceId: Flow<String>

    suspend fun getDeviceId(): String
    suspend fun setDeviceId(deviceId: String)
}
