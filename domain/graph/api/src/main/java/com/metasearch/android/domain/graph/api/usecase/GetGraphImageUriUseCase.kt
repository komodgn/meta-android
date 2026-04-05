package com.metasearch.android.domain.graph.api.usecase

interface GetGraphImageUriUseCase {
    suspend operator fun invoke(photoName: String): String?
}
