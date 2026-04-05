package com.metasearch.android.domain.graph.api.usecase

interface GetDetailGraphUrlUseCase {
    suspend operator fun invoke(entityName: String): String
}
