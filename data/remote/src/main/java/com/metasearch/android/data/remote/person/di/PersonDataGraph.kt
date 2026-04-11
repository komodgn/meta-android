package com.metasearch.android.data.remote.person.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.remote.person.service.PersonAIService
import com.metasearch.android.data.remote.person.service.PersonWebService
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import retrofit2.Retrofit

@ContributesTo(DataScope::class)
interface PersonDataGraph {
    @Provides
    fun providePersonWebService(@Named("WebRetrofit") retrofit: Retrofit): PersonWebService =
        retrofit.create(PersonWebService::class.java)

    @Provides
    fun providePersonAIService(@Named("AIRetrofit") retrofit: Retrofit): PersonAIService =
        retrofit.create(PersonAIService::class.java)
}
