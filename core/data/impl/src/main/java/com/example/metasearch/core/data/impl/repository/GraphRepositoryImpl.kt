package com.example.metasearch.core.data.impl.repository

import android.net.Uri
import androidx.core.net.toUri
import com.example.metasearch.core.data.api.repository.DatabaseNameRepository
import com.example.metasearch.core.data.api.repository.GalleryRepository
import com.example.metasearch.core.data.api.repository.GraphRepository
import com.example.metasearch.core.network.service.WebService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GraphRepositoryImpl @Inject constructor(
    private val databaseNameRepository: DatabaseNameRepository,
    private val galleryRepository: GalleryRepository,
    private val webService: WebService,
) : GraphRepository {
    private val webServerBaseUrl = com.example.metasearch.core.network.BuildConfig.WEB_SERVER_BASE_URL

    override suspend fun getFullGraphWebViewUrl(): String {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        return "$webServerBaseUrl/graph/$dbName"
    }

    override suspend fun getDetailGraphWebViewUrl(imageUriString: String): String {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
        val uri = imageUriString.toUri()

        val fileName = galleryRepository.getFileName(uri) ?: ""

        return "$webServerBaseUrl/entityTripleGraph/$dbName/$fileName"
    }

    override suspend fun getTripleData(photoName: String) {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        webService.fetchTripleData(dbName, photoName)
    }

    override suspend fun findMatchedUri(photoName: String): Uri? {
        return galleryRepository.findMatchedUri(photoName)
    }
}
