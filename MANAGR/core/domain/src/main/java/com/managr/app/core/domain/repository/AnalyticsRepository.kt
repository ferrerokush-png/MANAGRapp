package com.managr.app.core.domain.repository

import com.managr.app.core.domain.model.StreamingMetrics
import com.managr.app.core.domain.model.StreamingAnalytics
import com.managr.app.core.domain.model.StreamingPlatform
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for streaming analytics operations
 */
interface AnalyticsRepository {
    /**
     * Get streaming metrics for a project
     */
    fun getStreamingMetricsByProject(projectId: Long): Flow<List<StreamingMetrics>>

    /**
     * Get streaming metrics for a platform
     */
    fun getStreamingMetricsByPlatform(platform: StreamingPlatform, projectId: Long? = null): Flow<List<StreamingMetrics>>

    /**
     * Get streaming metrics for date range
     */
    fun getStreamingMetricsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
        projectId: Long? = null
    ): Flow<List<StreamingMetrics>>

    /**
     * Get latest streaming metrics
     */
    fun getLatestStreamingMetrics(projectId: Long? = null): Flow<List<StreamingMetrics>>

    /**
     * Get aggregated streaming analytics
     */
    fun getStreamingAnalytics(
        projectId: Long? = null,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<StreamingAnalytics>

    /**
     * Insert streaming metrics
     */
    suspend fun insertStreamingMetrics(metrics: StreamingMetrics): Long

    /**
     * Insert multiple streaming metrics
     */
    suspend fun insertStreamingMetrics(metricsList: List<StreamingMetrics>): List<Long>

    /**
     * Update streaming metrics
     */
    suspend fun updateStreamingMetrics(metrics: StreamingMetrics)

    /**
     * Delete streaming metrics
     */
    suspend fun deleteStreamingMetrics(metrics: StreamingMetrics)

    /**
     * Get total streams for project
     */
    suspend fun getTotalStreams(projectId: Long): Long

    /**
     * Get total streams for date range
     */
    suspend fun getTotalStreamsForDateRange(startDate: LocalDate, endDate: LocalDate): Long

    /**
     * Get all analytics data
     */
    fun getAllAnalytics(): Flow<List<StreamingMetrics>>

    /**
     * Delete all analytics data
     */
    suspend fun deleteAllAnalytics()
}
