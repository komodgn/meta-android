package com.metasearch.android.domain.analysis.api.usecase

interface GetImageDescriptionUseCase {
    suspend operator fun invoke(uriString: String): Result<String>
}
