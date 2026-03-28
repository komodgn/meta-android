package com.metasearch.android.feature.screens.context

import com.metasearch.android.core.di.context.ScreenContext
import com.metasearch.android.core.di.scope.NLSearchScope
import dev.zacsweers.metro.GraphExtension

@GraphExtension(NLSearchScope::class)
interface NLSearchScreenContext : ScreenContext {

    @GraphExtension.Factory()
    fun interface Factory {
        fun createNLSearchScreenContext(): NLSearchScreenContext
    }
}
