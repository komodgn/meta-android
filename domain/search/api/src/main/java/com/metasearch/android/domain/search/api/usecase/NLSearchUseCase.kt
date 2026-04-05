package com.metasearch.android.domain.search.api.usecase

import com.metasearch.android.data.domain.NLSearchResult

interface NLSearchUseCase {
    suspend operator fun invoke(query: String): Result<NLSearchResult>
}
