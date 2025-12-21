package com.example.metasearch.core.data.api.repository

interface DatabaseNameRepository {
    /**
     * @return db+deviceId 형식의 dbName(Neo4j Database Name)
     */
    suspend fun getPersistentDeviceDatabaseName(): String
}
