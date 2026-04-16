package com.metasearch.android.core.testing.rule

import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import coil3.ColorImage
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.test.FakeImageLoaderEngine
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CoilRule : TestWatcher() {

    @OptIn(DelicateCoilApi::class)
    override fun starting(description: Description?) {
        super.starting(description)
        val engine = FakeImageLoaderEngine.Builder()
            .default(ColorImage(Color.GRAY))
            .build()
        val imageLoader = ImageLoader.Builder(ApplicationProvider.getApplicationContext())
            .components { add(engine) }
            .build()
        SingletonImageLoader.setUnsafe(imageLoader)
    }
}
