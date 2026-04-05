package com.metasearch.android.data.graph.impl.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.graph.impl.repository.GraphRepositoryImpl
import com.metasearch.android.data.graph.impl.usecase.FetchTripleDataUseCaseImpl
import com.metasearch.android.data.graph.impl.usecase.GetDetailGraphUrlUseCaseImpl
import com.metasearch.android.data.graph.impl.usecase.GetFullGraphUrlUseCaseImpl
import com.metasearch.android.data.graph.impl.usecase.GetGraphImageUriUseCaseImpl
import com.metasearch.android.domain.graph.api.repository.GraphRepository
import com.metasearch.android.domain.graph.api.usecase.FetchTripleDataUseCase
import com.metasearch.android.domain.graph.api.usecase.GetDetailGraphUrlUseCase
import com.metasearch.android.domain.graph.api.usecase.GetFullGraphUrlUseCase
import com.metasearch.android.domain.graph.api.usecase.GetGraphImageUriUseCase
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
interface GraphDataGraph {
    @Binds
    val GraphRepositoryImpl.bind: GraphRepository

    @Binds
    val GetGraphImageUriUseCaseImpl.bind: GetGraphImageUriUseCase

    @Binds
    val GetFullGraphUrlUseCaseImpl.bind: GetFullGraphUrlUseCase

    @Binds
    val GetDetailGraphUrlUseCaseImpl.bind: GetDetailGraphUrlUseCase

    @Binds
    val FetchTripleDataUseCaseImpl.bind: FetchTripleDataUseCase
}
