package com.metasearch.android.domain.search.api.usecase

import com.metasearch.android.data.domain.Model

interface StartModelDownloadUseCase {
    operator fun invoke(model: Model)
}
