package com.metasearch.android.core.testing.repository

import android.net.Uri
import androidx.paging.PagingData
import com.metasearch.android.core.data.api.repository.GalleryRepository
import com.metasearch.android.core.model.GalleryImage
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

@Inject
public class FakeGalleryRepository : GalleryRepository {

    override fun getGalleryPagingData(): Flow<PagingData<GalleryImage>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllGalleryImages(): List<Uri> {
        TODO("Not yet implemented")
    }

    override suspend fun getFileName(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override suspend fun findMatchedUri(photoName: String): Uri? {
        TODO("Not yet implemented")
    }

    override suspend fun findMatchedUris(photoNames: List<String>): List<Uri> {
        TODO("Not yet implemented")
    }
}
