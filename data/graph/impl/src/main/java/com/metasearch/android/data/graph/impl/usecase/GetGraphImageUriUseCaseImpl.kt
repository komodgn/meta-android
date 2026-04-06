package com.metasearch.android.data.graph.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import dev.zacsweers.metro.Inject
import com.metasearch.android.domain.graph.api.usecase.GetGraphImageUriUseCase
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class GetGraphImageUriUseCaseImpl(
    private val galleryRepository: GalleryRepository,
) : GetGraphImageUriUseCase {

    override suspend fun invoke(photoName: String): String? {
        return galleryRepository.findMatchedUri(photoName)
    }
}
