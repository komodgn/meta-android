package com.metasearch.android.data.person.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.person.api.usecase.UpdateRepresentativeFaceUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class UpdateRepresentativeFaceUseCaseImpl(
    private val personRepository: PersonRepository,
) : UpdateRepresentativeFaceUseCase {

    override suspend fun invoke(personId: Long, faceId: Long): Result<Unit> =
        personRepository.updateRepresentativeFace(personId, faceId)
}
