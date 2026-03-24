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
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface DataGraph {

    @Binds
    val DatabaseNameRepositoryImpl.bind: DatabaseNameRepository

    @Binds
    val SearchRepositoryImpl.bind: SearchRepository

    @Binds
    val GalleryRepositoryImpl.bind: GalleryRepository

    @Binds
    val PersonRepositoryImpl.bind: PersonRepository

    @Binds
    val GraphRepositoryImpl.bind: GraphRepository

    @Binds
    val ImageAnalysisRepositoryImpl.bind: ImageAnalysisRepository
}
