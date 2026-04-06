package com.metasearch.android.data.graph.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.graph.api.usecase.FetchTripleDataUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class FetchTripleDataUseCaseImpl : FetchTripleDataUseCase {

    override suspend fun invoke(photoName: String) {
        TODO("Not yet implemented")
    }
}
