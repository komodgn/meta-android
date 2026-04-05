package com.metasearch.android.domain.graph.api.usecase

interface GetFullGraphUrlUseCase {
    suspend operator fun invoke(): String
}
