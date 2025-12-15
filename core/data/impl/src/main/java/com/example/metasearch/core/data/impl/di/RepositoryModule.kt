package com.example.metasearch.core.data.impl.di

import com.example.metasearch.core.data.api.repository.DatabaseNameRepository
import com.example.metasearch.core.data.impl.repository.DatabaseNameRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDatabaseNameRepository(deviceIdRepositoryImpl: DatabaseNameRepositoryImpl): DatabaseNameRepository
}
