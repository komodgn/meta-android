package com.metasearch.android.data.analysis.impl.repository

import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.core.room.api.dao.AnalyzedImageDao
import com.metasearch.android.core.room.api.entity.AnalyzedImageEntity
import com.metasearch.android.data.analysis.impl.mapper.toModel
import com.metasearch.android.data.domain.AnalysisResult
import com.metasearch.android.data.domain.UploadedImage
import com.metasearch.android.data.remote.analysis.AnalysisClient
import com.metasearch.android.domain.analysis.api.repository.AnalysisRepository
import com.metasearch.android.domain.device.api.repository.DatabaseNameRepository
import com.metasearch.android.domain.file.api.repository.FileRepository
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope

@Suppress("LongParameterList")
@SingleIn(DataScope::class)
@Inject
class AnalysisRepositoryImpl(
    private val analysisClient: AnalysisClient,
    private val databaseNameRepository: DatabaseNameRepository,
    private val galleryRepository: GalleryRepository,
    private val fileRepository: FileRepository,
    private val analyzedImageDao: AnalyzedImageDao,
) : AnalysisRepository {

    override suspend fun getAlreadyAnalyzedPaths(): List<String> =
        analyzedImageDao.getAllAnalyzedPaths()

    override suspend fun getFileNameByPath(path: String): String? =
        analyzedImageDao.getFileNameByPath(path)

    override suspend fun saveAnalyzedPath(path: String, fileName: String): Result<Unit> = runSuspendCatching {
        analyzedImageDao.insertPath(AnalyzedImageEntity(imagePath = path, fileName = fileName))
    }

    override suspend fun uploadImages(
        uriStrings: List<String>,
        dbName: String,
    ): List<UploadedImage> = supervisorScope {
        uriStrings.map { uriString ->
            async {
                runSuspendCatching {
                    val fileName = galleryRepository.getFileName(uriString) ?: "unknown.jpg"
                    val tempFile = fileRepository.createTempFileFromUri(uriString).getOrThrow()

                    try {
                        analysisClient.uploadImage(dbName, fileName, tempFile)
                        UploadedImage(uriString = uriString, fileName = fileName)
                    } finally {
                        fileRepository.deleteFile(tempFile)
                    }
                }.getOrNull()
            }
        }.awaitAll().filterNotNull()
    }

    override suspend fun deleteLocalPath(path: String): Result<Unit> = runSuspendCatching {
        analyzedImageDao.deletePath(path)
    }

    override suspend fun deleteImage(fileName: String, dbName: String): Result<Unit> = runSuspendCatching {
        analysisClient.deleteImage(fileName, dbName)
    }

    override suspend fun finishAnalysis(
        dbName: String,
        lastIndex: Int,
    ): Result<AnalysisResult> = runSuspendCatching {
        val response = analysisClient.finishAnalysis(dbName, lastIndex)

        response.toModel()
    }

    override suspend fun getImageTripleData(photoName: String): Result<String> = runSuspendCatching {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
        val response = analysisClient.fetchTripleData(dbName, photoName)

        response.triple
    }

    override suspend fun getAiCompletion(prompt: String): Result<String> = runSuspendCatching {
        analysisClient.getChatCompletion(prompt)
    }
}
