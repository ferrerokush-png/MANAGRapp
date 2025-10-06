package com.managr.app.core.domain.repository

import com.managr.app.core.domain.model.SocialMediaMetrics
import com.managr.app.core.domain.model.SocialMediaAnalytics
import com.managr.app.core.domain.model.SocialPlatform
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for social media analytics operations
 */
interface SocialMediaRepository {
    /**
     * Get social media metrics for a project
     */
    fun getSocialMediaMetricsByProject(projectId: Long): Flow<List<SocialMediaMetrics>>

    /**
     * Get social media metrics for a platform
     */
    fun getSocialMediaMetricsByPlatform(
        platform: SocialPlatform,
        projectId: Long? = null
    ): Flow<List<SocialMediaMetrics>>

    /**
     * Get social media metrics for date range
     */
    fun getSocialMediaMetricsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
        projectId: Long? = null
    ): Flow<List<SocialMediaMetrics>>

    /**
     * Get latest social media metrics
     */
    fun getLatestSocialMediaMetrics(projectId: Long? = null): Flow<List<SocialMediaMetrics>>

    /**
     * Get aggregated social media analytics
     */
    fun getSocialMediaAnalytics(
        projectId: Long? = null,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<SocialMediaAnalytics>

    /**
     * Insert social media metrics
     */
    suspend fun insertSocialMediaMetrics(metrics: SocialMediaMetrics): Long

    /**
     * Insert multiple social media metrics
     */
    suspend fun insertSocialMediaMetrics(metricsList: List<SocialMediaMetrics>): List<Long>

    /**
     * Update social media metrics
     */
    suspend fun updateSocialMediaMetrics(metrics: SocialMediaMetrics)

    /**
     * Delete social media metrics
     */
    suspend fun deleteSocialMediaMetrics(metrics: SocialMediaMetrics)

    /**
     * Get total followers across all platforms
     */
    suspend fun getTotalFollowers(): Long

    /**
     * Get total engagement for project
     */
    suspend fun getTotalEngagement(projectId: Long): Long
}
