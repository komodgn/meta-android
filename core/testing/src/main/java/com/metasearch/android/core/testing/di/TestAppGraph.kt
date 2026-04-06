package com.metasearch.android.core.testing.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.metasearch.android.core.data.api.repository.GalleryRepository
import com.metasearch.android.core.data.api.repository.GraphRepository
import com.metasearch.android.core.data.api.repository.ImageAnalysisRepository
import com.metasearch.android.core.data.api.repository.PersonRepository
import com.metasearch.android.core.data.api.repository.SearchRepository
import com.metasearch.android.core.data.impl.repository.GalleryRepositoryImpl
import com.metasearch.android.core.data.impl.repository.ImageAnalysisRepositoryImpl
import com.metasearch.android.core.data.impl.repository.PersonRepositoryImpl
import com.metasearch.android.core.data.impl.repository.SearchRepositoryImpl
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.core.testing.repository.FakeGalleryRepository
import com.metasearch.android.core.testing.repository.FakeGraphRepository
import com.metasearch.android.core.testing.repository.FakeImageAnalisisRepository
import com.metasearch.android.core.testing.repository.FakePersonRepository
import com.metasearch.android.core.testing.repository.FakeSearchRepository
import com.metasearch.android.core.testing.robot.core.CaptureScreenRobot
import com.metasearch.android.core.testing.robot.core.DefaultCaptureScreenRobot
import com.metasearch.android.feature.screens.context.NLSearchScreenContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraph
import kotlinx.coroutines.CoroutineDispatcher

@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
    excludes = [
        CoroutineDispatcher::class,
        GalleryRepositoryImpl::class,
        GraphRepositoryImpl::class,
        ImageAnalysisRepositoryImpl::class,
        PersonRepositoryImpl::class,
        SearchRepositoryImpl::class,
    ],
)
internal interface TestAppGraph :
    NLSearchScreenTestGraph {

    @SingleIn(AppScope::class)
    @Provides
    fun provideContext(): Context = ApplicationProvider.getApplicationContext()

    val nlSearchScreenContextFactory: NLSearchScreenContext.Factory

    @Provides
    fun provideCaptureScreenRobot(): CaptureScreenRobot = DefaultCaptureScreenRobot()

    @Binds
    val FakeGalleryRepository.binds: GalleryRepository

    @Binds
    val FakeGraphRepository.binds: GraphRepository

    @Binds
    val FakeImageAnalisisRepository.binds: ImageAnalysisRepository

    @Binds
    val FakePersonRepository.binds: PersonRepository

    @Binds
    val FakeSearchRepository.binds: SearchRepository
}

private fun initializeAndroidContext() {
    try {
        val testContext = ApplicationProvider.getApplicationContext<Context>()

        val providerClass = Class.forName("org.jetbrains.compose.resources.AndroidContextProvider")
        val provider = providerClass.getDeclaredConstructor().newInstance()

        val method = providerClass.getDeclaredMethods().find { it.name.contains("setANDROID_CONTEXT") }
        method?.let {
            it.isAccessible = true
            it.invoke(provider, testContext)
        }
    } catch (e: Exception) {
        println("Compose AndroidContextProvider initialization skipped: ${e.message}")
    }
}

internal fun createTestAppGraph(): TestAppGraph {
    initializeAndroidContext()

    return createGraph<TestAppGraph>()
}
