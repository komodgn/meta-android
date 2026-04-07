package com.metasearch.android.core.testing.repository

import androidx.paging.PagingData
import com.metasearch.android.data.domain.GalleryImage
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

@Inject
public class FakeGalleryRepository : GalleryRepository {
    override fun getGalleryPagingData(): Flow<PagingData<GalleryImage>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllGalleryImages(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getFileName(uriString: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun findMatchedUri(photoName: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun findMatchedUris(photoNames: List<String>): List<String> {
        TODO("Not yet implemented")
    }
}
