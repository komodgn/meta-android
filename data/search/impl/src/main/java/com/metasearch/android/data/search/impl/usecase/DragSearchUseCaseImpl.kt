package com.metasearch.android.data.search.impl.usecase

import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.data.domain.Circle
import com.metasearch.android.data.domain.DragSearchResult
import com.metasearch.android.domain.device.api.repository.DatabaseNameRepository
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.search.api.repository.SearchRepository
import com.metasearch.android.domain.search.api.usecase.DragSearchUseCase
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.File

@Inject
class DragSearchUseCaseImpl(
    private val galleryRepository: GalleryRepository,
    private val personRepository: PersonRepository,
    private val databaseNameRepository: DatabaseNameRepository,
    private val searchRepository: SearchRepository,
) : DragSearchUseCase {

    override suspend fun invoke(
        imageFile: File,
        circles: List<Circle>,
    ): Result<DragSearchResult> = runSuspendCatching {
        coroutineScope {
            val dbNameDeferred = async { databaseNameRepository.getPersistentDeviceDatabaseName() }

            val dbName = dbNameDeferred.await()
            val systemNames = searchRepository.analyzeFocusingImage(dbName, imageFile, circles)

            val mappedProperties = systemNames.map { systemName ->
                val inputName = personRepository.getInputNameBySystemName(systemName)

                if (inputName.isNullOrBlank()) systemName else inputName
            }.filter { it.isNotBlank() }.distinct()

            if (mappedProperties.isEmpty()) return@coroutineScope DragSearchResult(emptyList())

            val searchResult = searchRepository.findPhotosByDetectedObjects(dbName, mappedProperties)

            val updatedGroups = searchResult.groups.map { group ->
                async {
                    val matchedUris = galleryRepository.findMatchedUris(group.photoNames)

                    group.copy(photoNames = matchedUris)
                }
            }.awaitAll().filter { it.photoNames.isNotEmpty() }

            DragSearchResult(groups = updatedGroups)
        }
    }
}
