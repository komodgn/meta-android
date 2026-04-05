package com.metasearch.android.data.gallery.impl.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.gallery.impl.repository.GalleryRepositoryImpl
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
interface GalleryDataGraph {
    @Binds
    val GalleryRepositoryImpl.bind: GalleryRepository
}
