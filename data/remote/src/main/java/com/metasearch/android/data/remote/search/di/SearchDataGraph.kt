package com.metasearch.android.data.remote.search.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.remote.search.service.SearchAIService
import com.metasearch.android.data.remote.search.service.SearchOpenAIService
import com.metasearch.android.data.remote.search.service.SearchWebService
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import retrofit2.Retrofit

@ContributesTo(DataScope::class)
interface SearchDataGraph {
    @Provides
    fun provideSearchAIService(
        @Named("AIRetrofit") retrofit: Retrofit,
    ): SearchAIService = retrofit.create(SearchAIService::class.java)

    @Provides
    fun provideSearchWebService(
        @Named("WebRetrofit") retrofit: Retrofit,
    ): SearchWebService = retrofit.create(SearchWebService::class.java)

    @Provides
    fun provideSearchOpenAIService(
        @Named("OpenAIRetrofit") retrofit: Retrofit,
    ): SearchOpenAIService = retrofit.create(SearchOpenAIService::class.java)
}
