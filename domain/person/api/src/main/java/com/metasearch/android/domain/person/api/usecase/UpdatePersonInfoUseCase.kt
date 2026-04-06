package com.metasearch.android.domain.person.api.usecase

interface UpdatePersonInfoUseCase {
    suspend operator fun invoke(
        personId: Long,
        newName: String,
        newPhone: String,
        isHome: Boolean,
        faceId: Long?,
    ): Result<Long>
}
