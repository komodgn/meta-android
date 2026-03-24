package com.metasearch.android.core.notification.di

import android.content.Context
import com.metasearch.android.core.notification.notifier.AnalysisNotifier
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface NotificationGraph {

    @Provides
    fun provideAnalysisNotifier(context: Context): AnalysisNotifier {
        return AnalysisNotifier(context)
    }
}
