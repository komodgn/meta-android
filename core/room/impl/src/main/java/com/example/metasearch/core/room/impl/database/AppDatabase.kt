package com.example.metasearch.core.room.impl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.metasearch.core.room.api.dao.AnalyzedImageDao
import com.example.metasearch.core.room.api.dao.PersonDao
import com.example.metasearch.core.room.api.entity.AnalyzedImageEntity
import com.example.metasearch.core.room.api.entity.FaceEntity
import com.example.metasearch.core.room.api.entity.PersonEntity

@Database(
    entities = [
        PersonEntity::class,
        FaceEntity::class,
        AnalyzedImageEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun analyzedImageDao(): AnalyzedImageDao
}
