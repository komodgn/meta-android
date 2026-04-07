package com.metasearch.android.data.person.impl.usecase

import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.person.api.usecase.GetPersonPhotosUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class GetPersonPhotosUseCaseImpl(
    private val personRepository: PersonRepository,
    private val galleryRepository: GalleryRepository,
) : GetPersonPhotosUseCase {

    override suspend fun invoke(inputName: String): Result<List<String>> = runSuspendCatching {
        val photoNames = personRepository.getPersonPhotoNames(inputName).getOrThrow()

        galleryRepository.findMatchedUris(photoNames)
    }
}
