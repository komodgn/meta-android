package com.metasearch.android.core.room.api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.metasearch.android.core.room.api.entity.AnalyzedImageEntity

@Dao
interface AnalyzedImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPath(path: AnalyzedImageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPaths(entities: List<AnalyzedImageEntity>)

    @Query("DELETE FROM analyzed_images WHERE image_path = :path")
    suspend fun deletePath(path: String): Int

    @Query("SELECT image_path FROM analyzed_images")
    suspend fun getAllAnalyzedPaths(): List<String>

    @Query("SELECT file_name FROM analyzed_images WHERE image_path = :path LIMIT 1")
    suspend fun getFileNameByPath(path: String): String?

    @Transaction
    suspend fun runInTransaction(action: suspend () -> Unit) {
        action()
    }
}
