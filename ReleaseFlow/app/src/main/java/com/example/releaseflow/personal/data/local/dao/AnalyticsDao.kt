package com.example.releaseflow.personal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.releaseflow.personal.data.local.entity.Analytics
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalyticsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalytics(analytics: Analytics): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalyticsList(analyticsList: List<Analytics>): List<Long>

    @Update
    suspend fun updateAnalytics(analytics: Analytics): Int

    @Delete
    suspend fun deleteAnalytics(analytics: Analytics): Int

    @Query("SELECT * FROM analytics WHERE project_id = :projectId ORDER BY date DESC")
    fun getAnalyticsForProject(projectId: Long): Flow<List<Analytics>>

    @Query("SELECT * FROM analytics WHERE project_id = :projectId AND platform = :platform ORDER BY date DESC")
    fun getAnalyticsForProjectByPlatform(projectId: Long, platform: String): Flow<List<Analytics>>

    @Query("SELECT * FROM analytics WHERE id = :id LIMIT 1")
    fun getAnalyticsById(id: Long): Flow<Analytics?>

    @Query("DELETE FROM analytics WHERE id = :id")
    suspend fun deleteAnalyticsById(id: Long): Int
}
