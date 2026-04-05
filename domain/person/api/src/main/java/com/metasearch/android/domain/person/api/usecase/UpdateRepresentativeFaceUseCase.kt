package com.metasearch.android.domain.person.api.usecase

interface UpdateRepresentativeFaceUseCase {
    suspend operator fun invoke(personId: Long, faceId: Long): Result<Unit>
}
