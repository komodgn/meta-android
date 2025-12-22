package com.example.metasearch.core.notification.di

import android.content.Context
import com.example.metasearch.core.notification.notifier.AnalysisNotifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideAnalysisNotifier(@ApplicationContext context: Context): AnalysisNotifier {
        return AnalysisNotifier(context)
    }
}
