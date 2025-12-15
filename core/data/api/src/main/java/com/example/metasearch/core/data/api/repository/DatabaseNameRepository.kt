package com.example.metasearch.core.data.api.repository

interface DatabaseNameRepository {
    suspend fun getPersistentDeviceDatabaseName(): String
}
