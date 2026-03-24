package com.metasearch.android.core.room.impl.di

import android.content.Context
import androidx.room.Room
import com.metasearch.android.core.room.api.dao.AnalyzedImageDao
import com.metasearch.android.core.room.api.dao.PersonDao
import com.metasearch.android.core.room.impl.database.AppDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlin.jvm.java

private const val DATABASE_NAME = "METASEARCH_APP_DB"

@ContributesTo(AppScope::class)
interface DatabaseGraph {

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME,
        )
            .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_3_4)
            .build()
    }

    @Provides
    fun providePersonDao(appDatabase: AppDatabase): PersonDao {
        return appDatabase.personDao()
    }

    @Provides
    fun provideAnalyzedImageDao(appDatabase: AppDatabase): AnalyzedImageDao {
        return appDatabase.analyzedImageDao()
    }
}
