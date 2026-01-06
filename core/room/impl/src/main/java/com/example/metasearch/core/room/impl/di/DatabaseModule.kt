package com.example.metasearch.core.room.impl.di

import android.content.Context
import androidx.room.Room
import com.example.metasearch.core.room.api.dao.AnalyzedImageDao
import com.example.metasearch.core.room.api.dao.PersonDao
import com.example.metasearch.core.room.impl.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val DATABASE_NAME = "METASEARCH_APP_DB"

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME,
        )
            .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_3_4)
            .build()
    }

    @Provides
    @Singleton
    fun providePersonDao(appDatabase: AppDatabase): PersonDao {
        return appDatabase.personDao()
    }

    @Provides
    @Singleton
    fun provideAnalyzedImageDao(appDatabase: AppDatabase): AnalyzedImageDao {
        return appDatabase.analyzedImageDao()
    }
}
