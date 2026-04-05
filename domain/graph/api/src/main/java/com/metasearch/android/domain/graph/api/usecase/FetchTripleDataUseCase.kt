package com.metasearch.android.domain.graph.api.usecase

interface FetchTripleDataUseCase {
    suspend operator fun invoke(photoName: String)
}
