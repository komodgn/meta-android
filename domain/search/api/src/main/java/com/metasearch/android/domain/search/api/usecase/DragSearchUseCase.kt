package com.metasearch.android.domain.search.api.usecase

import com.metasearch.android.data.domain.Circle
import com.metasearch.android.data.domain.DragSearchResult
import java.io.File

interface DragSearchUseCase {
    suspend operator fun invoke(imageFile: File, circles: List<Circle>): Result<DragSearchResult>
}
