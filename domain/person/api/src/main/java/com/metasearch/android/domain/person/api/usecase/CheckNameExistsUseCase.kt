package com.metasearch.android.domain.person.api.usecase

interface CheckNameExistsUseCase {
    suspend operator fun invoke(name: String): Boolean
}
