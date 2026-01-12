package com.metasearch.android.core.data.impl.di

import com.metasearch.android.core.data.api.repository.DatabaseNameRepository
import com.metasearch.android.core.data.api.repository.GalleryRepository
import com.metasearch.android.core.data.api.repository.GraphRepository
import com.metasearch.android.core.data.api.repository.ImageAnalysisRepository
import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.core.data.api.repository.SearchRepository
import com.metasearch.android.core.data.impl.repository.DatabaseNameRepositoryImpl
import com.metasearch.android.core.data.impl.repository.GalleryRepositoryImpl
import com.metasearch.android.core.data.impl.repository.GraphRepositoryImpl
import com.metasearch.android.core.data.impl.repository.ImageAnalysisRepositoryImpl
import com.metasearch.android.core.data.impl.repository.PersonRepositoryImpl
import com.metasearch.android.core.data.impl.repository.SearchRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository

    @Binds
    @Singleton
    abstract fun bindGalleryRepository(galleryRepositoryImpl: GalleryRepositoryImpl): GalleryRepository

    @Binds
    @Singleton
    abstract fun bindPersonRepository(personRepositoryImpl: PersonRepositoryImpl): PersonRepository

    @Binds
    @Singleton
    abstract fun bindGraphRepository(graphRepositoryImpl: GraphRepositoryImpl): GraphRepository

    @Binds
    @Singleton
    abstract fun bindImageAnalysisRepository(imageAnalysisRepositoryImpl: ImageAnalysisRepositoryImpl): ImageAnalysisRepository
}
