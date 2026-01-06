package com.example.metasearch.core.room.impl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 4,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE persons ADD COLUMN representative_face INTEGER DEFAULT NULL")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE analyzed_images ADD COLUMN file_name TEXT NOT NULL DEFAULT 'unknown.jpg'")
            }
        }
    }

    abstract fun personDao(): PersonDao
    abstract fun analyzedImageDao(): AnalyzedImageDao
}
