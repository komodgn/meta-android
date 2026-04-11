package com.metasearch.android.data.graph.impl.repository

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.remote.BuildConfig
import com.metasearch.android.domain.device.api.repository.DatabaseNameRepository
import com.metasearch.android.domain.graph.api.repository.GraphRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.net.URLEncoder

@SingleIn(DataScope::class)
@Inject
class GraphRepositoryImpl(
    private val databaseNameRepository: DatabaseNameRepository,
) : GraphRepository {
    private val webServerBaseUrl = BuildConfig.WEB_SERVER_BASE_URL

    override suspend fun getFullGraphWebViewUrl(): String {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
        return "$webServerBaseUrl/graph/$dbName"
    }

    override suspend fun getDetailGraphWebViewUrl(entityName: String): String {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
        val encodedName = URLEncoder.encode(entityName, "UTF-8")

        return "$webServerBaseUrl/entityTripleGraph/$dbName/$encodedName"
    }
}
