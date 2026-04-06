package com.metasearch.android.data.graph.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.graph.api.repository.GraphRepository
import com.metasearch.android.domain.graph.api.usecase.GetDetailGraphUrlUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class GetDetailGraphUrlUseCaseImpl(
    private val graphRepository: GraphRepository,
) : GetDetailGraphUrlUseCase {

    override suspend fun invoke(entityName: String): String = graphRepository.getDetailGraphWebViewUrl(entityName)
}
