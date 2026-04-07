package com.metasearch.android.data.analysis.impl.usecase

import android.util.Log
import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.datastore.api.datasource.PersonIndexDataSource
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.core.room.api.dao.AnalyzedImageDao
import com.metasearch.android.data.domain.AnalysisResult
import com.metasearch.android.data.domain.DetectedPerson
import com.metasearch.android.data.domain.UploadedImage
import com.metasearch.android.domain.analysis.api.repository.AnalysisRepository
import com.metasearch.android.domain.analysis.api.usecase.ProcessAnalysisResultUseCase
import com.metasearch.android.domain.person.api.repository.PersonRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SingleIn(DataScope::class)
@Inject
class ProcessAnalysisResultUseCaseImpl(
    private val analysisRepository: AnalysisRepository,
    private val personRepository: PersonRepository,
    private val personIndexDataSource: PersonIndexDataSource,
    private val analyzedImageDao: AnalyzedImageDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ProcessAnalysisResultUseCase {

    companion object {
        private const val TAG = "ProcessAnalysisResult"
    }

    override suspend fun invoke(
        dbName: String,
        successfulPaths: List<UploadedImage>,
    ): Result<AnalysisResult> = withContext(ioDispatcher) {
        val lastIndex = personIndexDataSource.getLastPersonIndex()

        analysisRepository.finishAnalysis(dbName, lastIndex)
            .onSuccess { result ->
                Log.d(TAG, "Server response received: ${result.detectedPersons.size} persons")
                analyzedImageDao.runInTransaction {
                    val newMax = result.detectedPersons
                        .mapNotNull { it.imageName?.filter { c -> c.isDigit() }?.toIntOrNull() }
                        .maxOrNull() ?: lastIndex

                    if (newMax > lastIndex) {
                        personIndexDataSource.setLastPersonIndex(newMax)
                    }

                    result.detectedPersons.forEach { person ->
                        Log.d(TAG, "Processing person: ${person.imageName}, faceExists: ${person.isFaceExist}")
                        processSinglePerson(person)
                    }

                    successfulPaths.forEach { (path, fileName) ->
                        analysisRepository.saveAnalyzedPath(path, fileName)
                    }
                }
            }
            .onFailure { e ->
                Log.e(TAG, "Failed to finish analysis on server: ${e.message}", e)
            }
    }

    private suspend fun processSinglePerson(person: DetectedPerson) {
        runSuspendCatching {
            if (person.isFaceExist && person.imageName != null && person.imageBytes != null) {
                val existingPersonId = personRepository.getPersonIdByImageName(person.imageName!!)

                if (existingPersonId != null) {
                    personRepository.addFaceToExistingPerson(
                        personId = existingPersonId,
                        imageName = person.imageName!!,
                        imageBytes = person.imageBytes!!,
                    )
                } else {
                    personRepository.addAnalyzedPerson(
                        imageName = person.imageName!!,
                        imageBytes = person.imageBytes!!,
                    )
                }
                Log.d(TAG, "Successfully saved to DB: ${person.imageName}")
            } else {
                Log.w(TAG, "Skipped saving: Missing data or no face detected for ${person.imageName}")
            }
        }.onFailure { e ->
            Log.e(TAG, "Failed processSinglePerson: ${e.message}", e)
        }
    }
}
