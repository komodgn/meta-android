package com.metasearch.android.data.analysis.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.analysis.api.repository.AnalysisRepository
import com.metasearch.android.domain.analysis.api.usecase.ProcessAnalysisResultUseCase
import com.metasearch.android.domain.analysis.api.usecase.StartFullAnalysisUseCase
import com.metasearch.android.domain.device.api.repository.DatabaseNameRepository
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.search.api.repository.SearchRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SingleIn(DataScope::class)
@Inject
class StartFullAnalysisUseCaseImpl(
    private val analysisRepository: AnalysisRepository,
    private val galleryRepository: GalleryRepository,
    private val personRepository: PersonRepository,
    private val databaseNameRepository: DatabaseNameRepository,
    private val processAnalysisResultUseCase: ProcessAnalysisResultUseCase,
    private val searchRepository: SearchRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : StartFullAnalysisUseCase {

    companion object {
        private const val CHUNK_SIZE = 10
    }

    override suspend fun invoke() = withContext(ioDispatcher) {
        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()

        val currentUriStrings = galleryRepository.getAllGalleryImages()
        val alreadyAnalyzed = analysisRepository.getAlreadyAnalyzedPaths()

        val deletePaths = alreadyAnalyzed.filter { it !in currentUriStrings }
        if (deletePaths.isNotEmpty()) {
            deletePaths.forEach { path ->
                val fileName = analysisRepository.getFileNameByPath(path)
                analysisRepository.deleteImage(fileName ?: "unknown.jpg", dbName)
                    .onSuccess {
                        analysisRepository.deleteLocalPath(path)
                    }
            }
//            searchRepository.clearEntityCache()
        }

        val addUriStrings = currentUriStrings.filter { it !in alreadyAnalyzed }
        if (addUriStrings.isNotEmpty()) {
            val successfulData = mutableListOf<Pair<String, String>>()

            addUriStrings.chunked(CHUNK_SIZE).forEach { chunk ->
                val results = analysisRepository.uploadImages(chunk, dbName)
                successfulData.addAll(results)
            }

            if (successfulData.isNotEmpty()) {
                processAnalysisResultUseCase(dbName, successfulData)
//                searchRepository.clearEntityCache()
            }
        }

        syncMismatchedNames(dbName)
    }

    private suspend fun syncMismatchedNames(dbName: String) {
        val mismatches = personRepository.getMismatchedFaceNames()

        mismatches.forEach { (serverName, actualName) ->
            personRepository.changePersonNameOnServer(
                oldName = serverName,
                newName = actualName,
            )
        }
    }
}
