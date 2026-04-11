package com.metasearch.android.data.remote.analysis.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.remote.analysis.service.AnalysisAIService
import com.metasearch.android.data.remote.analysis.service.AnalysisOpenAIService
import com.metasearch.android.data.remote.analysis.service.AnalysisWebService
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import retrofit2.Retrofit

@ContributesTo(DataScope::class)
interface AnalysisDataGraph {
    @Provides
    fun provideAnalysisAIService(@Named("AIRetrofit") retrofit: Retrofit): AnalysisAIService =
        retrofit.create(AnalysisAIService::class.java)

    @Provides
    fun provideAnalysisWebService(@Named("WebRetrofit") retrofit: Retrofit): AnalysisWebService =
        retrofit.create(AnalysisWebService::class.java)

    @Provides
    fun provideAnalysisOpenAIService(@Named("OpenAIRetrofit") retrofit: Retrofit): AnalysisOpenAIService =
        retrofit.create(AnalysisOpenAIService::class.java)
}
