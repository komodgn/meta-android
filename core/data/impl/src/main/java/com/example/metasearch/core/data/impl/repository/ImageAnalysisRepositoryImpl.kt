package com.example.metasearch.core.data.impl.repository

import android.content.Context
import android.util.Log
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.metasearch.core.data.api.repository.DatabaseNameRepository
import com.example.metasearch.core.data.api.repository.GalleryRepository
import com.example.metasearch.core.data.api.repository.ImageAnalysisRepository
import com.example.metasearch.core.data.api.repository.PersonRepository
import com.example.metasearch.core.network.request.ChangeNameRequest
import com.example.metasearch.core.network.service.AIService
import com.example.metasearch.core.network.service.WebService
import com.example.metasearch.core.room.api.dao.AnalyzedImageDao
import com.example.metasearch.core.room.api.entity.AnalyzedImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ImageAnalysisRepositoryImpl @Inject constructor(
    private val analyzedImageDao: AnalyzedImageDao,
    private val galleryRepository: GalleryRepository,
    private val databaseNameRepository: DatabaseNameRepository,
    private val personRepository: PersonRepository,
    private val aiService: AIService,
    private val webService: WebService,
) : ImageAnalysisRepository {
    private val tag = "ImageAnalysisRepo"

    override fun getAnalysisStatus(context: Context): Flow<Boolean> {
        return WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow("ImageAnalysisWork")
            .map { workInfos ->
                workInfos.any { it.state == WorkInfo.State.RUNNING }
            }
    }

    override suspend fun runFullAnalysis() = withContext(Dispatchers.IO) {
        Log.d(tag, "runFullAnalysis 함수 실행")

        val currentGalleryPaths = galleryRepository.getAllGalleryPaths()
        val alreadyAnalyzedPaths = analyzedImageDao.getAllAnalyzedPaths()
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        val deletePaths = alreadyAnalyzedPaths.filter { it !in currentGalleryPaths }
        val addPaths = currentGalleryPaths.filter { path ->
            path !in alreadyAnalyzedPaths &&
                (path.endsWith(".jpg", true) || path.endsWith(".jpeg", true) || path.endsWith(".png", true))
        }
        Log.d(tag, "deletePaths size : " + deletePaths.size)
        Log.d(tag, "addPaths size : " + addPaths.size)

        if (deletePaths.isEmpty() && addPaths.isEmpty()) return@withContext

        deletePaths.forEach { path ->
            val fileNamePart = createMultipartBodyPartFromFilePath("deleteImage", path)
            val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

            runCatching { webService.uploadWebDeleteImage(fileNamePart, dbName) }
            runCatching { aiService.uploadDeleteImage(fileNamePart, dbNameBody) }

            analyzedImageDao.deletePath(path)
        }

        addPaths.forEach { path ->
            val webImagePart = createMultipartBodyPartFromFilePath("image", path)
            val aiImagePart = createMultipartBodyPartFromFilePath("addImage", path)
            val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())

            runCatching { webService.uploadWebAddImage(webImagePart, dbName) }
            runCatching { aiService.uploadAddImage(aiImagePart, dbNameBody) }

            analyzedImageDao.insertPath(AnalyzedImageEntity(imagePath = path))
        }

        val finishBody = "true".toRequestBody("text/plain".toMediaTypeOrNull())
        val dbNameBody = dbName.toRequestBody("text/plain".toMediaTypeOrNull())
        val countBody = "0".toRequestBody("text/plain".toMediaTypeOrNull())

        runCatching { aiService.uploadFinish(finishBody, dbNameBody, countBody) }

        val mismatchedNames = personRepository.getMismatchedNames()
        mismatchedNames.forEach { (oldName, newName) ->
            webService.changeName(ChangeNameRequest(dbName, oldName, newName))
        }
    }

    private fun createMultipartBodyPartFromFilePath(name: String, path: String): MultipartBody.Part {
        val file = File(path)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }
}
