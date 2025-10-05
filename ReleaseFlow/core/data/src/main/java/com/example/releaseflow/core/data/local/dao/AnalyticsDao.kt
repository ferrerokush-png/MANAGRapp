package com.example.releaseflow.core.data.local.dao

import androidx.room.*
import com.example.releaseflow.core.data.local.entity.StreamingMetricsEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface AnalyticsDao {
    @Query("SELECT * FROM streaming_metrics WHERE projectId = :projectId ORDER BY date DESC")
    fun getStreamingMetricsByProject(projectId: Long): Flow<List<StreamingMetricsEntity>>

    @Query("SELECT * FROM streaming_metrics WHERE platform = :platform ORDER BY date DESC")
    fun getStreamingMetricsByPlatform(platform: String): Flow<List<StreamingMetricsEntity>>

    @Query("SELECT * FROM streaming_metrics WHERE platform = :platform AND projectId = :projectId ORDER BY date DESC")
    fun getStreamingMetricsByPlatformAndProject(platform: String, projectId: Long): Flow<List<StreamingMetricsEntity>>

    @Query("SELECT * FROM streaming_metrics WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getStreamingMetricsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<StreamingMetricsEntity>>

    @Query("SELECT * FROM streaming_metrics WHERE projectId = :projectId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getStreamingMetricsByProjectAndDateRange(projectId: Long, startDate: LocalDate, endDate: LocalDate): Flow<List<StreamingMetricsEntity>>

    @Query("SELECT * FROM streaming_metrics ORDER BY date DESC LIMIT 30")
    fun getLatestStreamingMetrics(): Flow<List<StreamingMetricsEntity>>

    @Query("SELECT * FROM streaming_metrics WHERE projectId = :projectId ORDER BY date DESC LIMIT 30")
    fun getLatestStreamingMetricsByProject(projectId: Long): Flow<List<StreamingMetricsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStreamingMetrics(metrics: StreamingMetricsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStreamingMetrics(metricsList: List<StreamingMetricsEntity>): List<Long>

    @Update
    suspend fun updateStreamingMetrics(metrics: StreamingMetricsEntity)

    @Delete
    suspend fun deleteStreamingMetrics(metrics: StreamingMetricsEntity)

    @Query("SELECT SUM(streams) FROM streaming_metrics WHERE projectId = :projectId")
    suspend fun getTotalStreams(projectId: Long): Long?

    @Query("SELECT SUM(streams) FROM streaming_metrics WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalStreamsForDateRange(startDate: LocalDate, endDate: LocalDate): Long?
}
