package com.example.metasearch.core.data.api.repository

interface DatabaseNameRepository {
    /**
     * db+deviceId 형식으로 dbName(Neo4j Database Name) 반환
     */
    suspend fun getPersistentDeviceDatabaseName(): String
}
