package com.example.releaseflow.personal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.releaseflow.personal.data.local.entity.AIQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface AIQueryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuery(query: AIQuery): Long

    @Query("SELECT * FROM ai_queries ORDER BY created_at DESC LIMIT :limit")
    fun getRecentQueries(limit: Int = 20): Flow<List<AIQuery>>

    @Query("SELECT * FROM ai_queries WHERE project_id = :projectId ORDER BY created_at DESC")
    fun getQueriesForProject(projectId: Long): Flow<List<AIQuery>>

    @Query("SELECT COUNT(*) FROM ai_queries WHERE created_at >= :sinceTimestamp")
    suspend fun getQueryCountSince(sinceTimestamp: Long): Int

    @Update
    suspend fun updateQuery(query: AIQuery)

    @Query("DELETE FROM ai_queries WHERE id = :id")
    suspend fun deleteQuery(id: Long)
}
