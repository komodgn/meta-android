package com.metasearch.android.domain.person.api.usecase

interface GetPersonPhotosUseCase {
    suspend operator fun invoke(inputName: String): Result<List<String>>
}
