package com.metasearch.android.core.data.impl.repository

import android.net.Uri
import com.metasearch.android.core.data.api.repository.DatabaseNameRepository
import com.metasearch.android.core.data.api.repository.GalleryRepository
import com.metasearch.android.core.data.api.repository.GraphRepository
import com.metasearch.android.core.network.BuildConfig
import com.metasearch.android.core.network.service.WebService
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GraphRepositoryImpl @Inject constructor(
    private val databaseNameRepository: DatabaseNameRepository,
    private val galleryRepository: GalleryRepository,
    private val webService: WebService,
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

    override suspend fun getTripleData(photoName: String) {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        webService.fetchTripleData(dbName, photoName)
    }

    override suspend fun findMatchedUri(photoName: String): Uri? {
        return galleryRepository.findMatchedUri(photoName)
    }
}
