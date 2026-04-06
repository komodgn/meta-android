package com.metasearch.android.data.graph.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.graph.api.repository.GraphRepository
import com.metasearch.android.domain.graph.api.usecase.GetFullGraphUrlUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class GetFullGraphUrlUseCaseImpl(
    private val graphRepository: GraphRepository,
) : GetFullGraphUrlUseCase {

    override suspend fun invoke(): String = graphRepository.getFullGraphWebViewUrl()
}
