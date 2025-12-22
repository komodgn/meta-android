package com.example.metasearch.core.room.api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.metasearch.core.room.api.entity.AnalyzedImageEntity

@Dao
interface AnalyzedImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPath(path: AnalyzedImageEntity): Long

    @Query("DELETE FROM analyzed_images WHERE image_path = :path")
    suspend fun deletePath(path: String): Int

    @Query("SELECT image_path FROM analyzed_images")
    suspend fun getAllAnalyzedPaths(): List<String>
}
