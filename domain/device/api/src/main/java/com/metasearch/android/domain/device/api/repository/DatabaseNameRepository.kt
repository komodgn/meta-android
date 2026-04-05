package com.metasearch.android.domain.device.api.repository

interface DatabaseNameRepository {
    suspend fun getPersistentDeviceDatabaseName(): String
}
