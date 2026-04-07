package com.metasearch.android.data.analysis.impl.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.analysis.impl.repository.AnalysisRepositoryImpl
import com.metasearch.android.data.analysis.impl.usecase.GetImageDescriptionUseCaseImpl
import com.metasearch.android.data.analysis.impl.usecase.ProcessAnalysisResultUseCaseImpl
import com.metasearch.android.data.analysis.impl.usecase.StartFullAnalysisUseCaseImpl
import com.metasearch.android.domain.analysis.api.repository.AnalysisRepository
import com.metasearch.android.domain.analysis.api.usecase.GetImageDescriptionUseCase
import com.metasearch.android.domain.analysis.api.usecase.ProcessAnalysisResultUseCase
import com.metasearch.android.domain.analysis.api.usecase.StartFullAnalysisUseCase
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
interface AnalysisDataGraph {
    @Binds
    val AnalysisRepositoryImpl.bind: AnalysisRepository

    @Binds
    val GetImageDescriptionUseCaseImpl.bind: GetImageDescriptionUseCase

    @Binds
    val ProcessAnalysisResultUseCaseImpl.bind: ProcessAnalysisResultUseCase

    @Binds
    val StartFullAnalysisUseCaseImpl.bind: StartFullAnalysisUseCase
}
