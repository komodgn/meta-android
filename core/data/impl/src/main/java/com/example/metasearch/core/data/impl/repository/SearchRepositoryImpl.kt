package com.example.metasearch.core.data.impl.repository

import com.example.metasearch.core.data.api.repository.DatabaseNameRepository
import com.example.metasearch.core.data.api.repository.SearchRepository
import com.example.metasearch.core.data.impl.mapper.toModel
import com.example.metasearch.core.model.CircleModel
import com.example.metasearch.core.network.request.Circle as RequestCircle
import com.example.metasearch.core.model.SearchResult
import com.example.metasearch.core.network.request.DetectedObjectsRequest
import com.example.metasearch.core.network.request.FocusingSearchRequest
import com.example.metasearch.core.network.service.AIService
import com.example.metasearch.core.network.service.WebService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
internal class SearchRepositoryImpl @Inject constructor(
    private val aiService: AIService,
    private val webService: WebService,
    private val databaseNameRepository: DatabaseNameRepository,
) : SearchRepository {
    override suspend fun focusingSearch(
        imageFile: File,
        circles: List<CircleModel>
    ): Result<SearchResult> = runCatching {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        val imagePart = MultipartBody.Part.createFormData(
            "searchImage",
            imageFile.name,
            imageFile.asRequestBody("image/jpeg".toMediaType()),
        )

        val dbNamePart = dbName.toRequestBody("text/plain".toMediaType())

        val requestCircles = circles.map {
            RequestCircle(it.centerX, it.centerY, it.radius)
        }
        val detectionResponse = aiService.uploadImageAndCircles(
            image = imagePart,
            dbName = dbNamePart,
            request = FocusingSearchRequest(requestCircles),
        )

        val finalResult = webService.sendDetectedObjects(
            request = DetectedObjectsRequest(
                dbName = dbName,
                properties = detectionResponse.detectedObjects,
            )
        )

        finalResult?.toModel() ?: SearchResult(emptyList())
    }
}
