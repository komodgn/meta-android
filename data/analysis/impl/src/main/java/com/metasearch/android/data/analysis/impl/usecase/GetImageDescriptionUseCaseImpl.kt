package com.metasearch.android.data.analysis.impl.usecase

import com.metasearch.android.core.common.constants.PromptConstants
import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.analysis.api.repository.AnalysisRepository
import com.metasearch.android.domain.analysis.api.usecase.GetImageDescriptionUseCase
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class GetImageDescriptionUseCaseImpl(
    private val analysisRepository: AnalysisRepository,
    private val galleryRepository: GalleryRepository,
) : GetImageDescriptionUseCase {

    override suspend fun invoke(uriString: String): Result<String> = runSuspendCatching {
        val photoName = galleryRepository.getFileName(uriString) ?: throw IllegalArgumentException("File not found.")

        val tripleData = analysisRepository.getImageTripleData(photoName)

        val fullPrompt = PromptConstants.CREATE_IMAGE_BASIC_PROMPT + tripleData

        analysisRepository.getAiCompletion(fullPrompt).getOrThrow()
    }
}
