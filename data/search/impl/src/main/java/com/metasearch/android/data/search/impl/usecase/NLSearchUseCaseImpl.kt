package com.metasearch.android.data.search.impl.usecase

import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.domain.NLSearchResult
import com.metasearch.android.domain.device.api.repository.DatabaseNameRepository
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import com.metasearch.android.domain.search.api.repository.SearchRepository
import com.metasearch.android.domain.search.api.usecase.NLSearchUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class NLSearchUseCaseImpl(
    private val searchRepository: SearchRepository,
    private val galleryRepository: GalleryRepository,
    private val databaseNameRepository: DatabaseNameRepository,
) : NLSearchUseCase {

    companion object {
        private const val CACHE_SIZE = 30
    }

    private val entityCache = androidx.collection.LruCache<String, List<String>>(CACHE_SIZE)

    override suspend fun invoke(query: String, isLocal: Boolean): Result<NLSearchResult> = runSuspendCatching {
        if (query.isBlank()) return@runSuspendCatching NLSearchResult(emptyList())

        val entities = if (isLocal) {
            searchRepository.extractKeywordsFromLocalNL(query)
        } else {
            searchRepository.extractKeywordsFromNL(query)
        }

        if (entities.isEmpty()) return@runSuspendCatching NLSearchResult(emptyList())

        val entityKey = entities.joinToString(",")
        entityCache.get(entityKey)?.let { cachedUris ->
            return@runSuspendCatching NLSearchResult(cachedUris)
        }

        val dbName = databaseNameRepository.getPersistentDeviceDatabaseName()
        val photoNames = searchRepository.searchPhotosByKeywords(dbName, entities)

        val matchedUris = galleryRepository.findMatchedUris(photoNames)

        val finalResult = NLSearchResult(matchedUris = matchedUris)

        entityCache.put(entityKey, matchedUris)

        finalResult
    }
}
