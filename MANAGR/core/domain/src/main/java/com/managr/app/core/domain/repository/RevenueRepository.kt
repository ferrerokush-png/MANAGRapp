package com.managr.app.core.domain.repository

import com.managr.app.core.domain.model.Revenue
import com.managr.app.core.domain.model.RevenueAnalytics
import com.managr.app.core.domain.model.RevenueProjection
import com.managr.app.core.domain.model.StreamingPlatform
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

/**
 * Repository interface for revenue operations
 */
interface RevenueRepository {
    /**
     * Get all revenue records
     */
    fun getAllRevenue(): Flow<List<Revenue>>

    /**
     * Get revenue for a project
     */
    fun getRevenueByProject(projectId: Long): Flow<List<Revenue>>

    /**
     * Get revenue for a platform
     */
    fun getRevenueByPlatform(platform: StreamingPlatform, projectId: Long? = null): Flow<List<Revenue>>

    /**
     * Get revenue for a period
     */
    fun getRevenueByPeriod(period: YearMonth, projectId: Long? = null): Flow<List<Revenue>>

    /**
     * Get revenue for period range
     */
    fun getRevenueByPeriodRange(
        startPeriod: YearMonth,
        endPeriod: YearMonth,
        projectId: Long? = null
    ): Flow<List<Revenue>>

    /**
     * Get unpaid revenue
     */
    fun getUnpaidRevenue(projectId: Long? = null): Flow<List<Revenue>>

    /**
     * Get aggregated revenue analytics
     */
    fun getRevenueAnalytics(
        projectId: Long? = null,
        startPeriod: YearMonth,
        endPeriod: YearMonth
    ): Flow<RevenueAnalytics>

    /**
     * Get revenue projections
     */
    fun getRevenueProjections(projectId: Long? = null, months: Int = 6): Flow<List<RevenueProjection>>

    /**
     * Insert revenue record
     */
    suspend fun insertRevenue(revenue: Revenue): Long

    /**
     * Insert multiple revenue records
     */
    suspend fun insertRevenue(revenueList: List<Revenue>): List<Long>

    /**
     * Update revenue record
     */
    suspend fun updateRevenue(revenue: Revenue)

    /**
     * Mark revenue as paid
     */
    suspend fun markAsPaid(revenueId: Long)

    /**
     * Delete revenue record
     */
    suspend fun deleteRevenue(revenue: Revenue)

    /**
     * Get total revenue
     */
    suspend fun getTotalRevenue(projectId: Long? = null): Double

    /**
     * Get total unpaid revenue
     */
    suspend fun getTotalUnpaidRevenue(projectId: Long? = null): Double
}
