package com.metasearch.android.data.search.impl.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.search.impl.repository.SearchRepositoryImpl
import com.metasearch.android.data.search.impl.usecase.DragSearchUseCaseImpl
import com.metasearch.android.data.search.impl.usecase.NLSearchUseCaseImpl
import com.metasearch.android.domain.search.api.repository.SearchRepository
import com.metasearch.android.domain.search.api.usecase.DragSearchUseCase
import com.metasearch.android.domain.search.api.usecase.NLSearchUseCase
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
interface SearchDataGraph {
    @Binds
    val SearchRepositoryImpl.bind: SearchRepository

    @Binds
    val DragSearchUseCaseImpl.bind: DragSearchUseCase

    @Binds
    val NLSearchUseCaseImpl.bind: NLSearchUseCase
}
