package com.example.releaseflow.core.domain.model

import java.time.LocalDate
import java.time.YearMonth

/**
 * Revenue record for a specific period and source
 */
data class Revenue(
    val id: Long = 0,
    val projectId: Long? = null, // null means overall artist revenue
    val platform: StreamingPlatform,
    val period: YearMonth,
    val streams: Long = 0,
    val amount: Double = 0.0, // in USD
    val currency: String = "USD",
    val payoutDate: LocalDate? = null,
    val isPaid: Boolean = false,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Calculate revenue per stream
     */
    fun revenuePerStream(): Double {
        return if (streams > 0) amount / streams else 0.0
    }

    /**
     * Check if payout is overdue (more than 90 days after period end)
     */
    fun isPayoutOverdue(): Boolean {
        if (isPaid) return false
        val periodEnd = period.atEndOfMonth()
        val expectedPayoutDate = periodEnd.plusDays(90)
        return LocalDate.now().isAfter(expectedPayoutDate)
    }

    /**
     * Estimate days until payout
     */
    fun daysUntilPayout(): Long? {
        if (isPaid) return null
        val estimatedPayoutDate = payoutDate ?: period.atEndOfMonth().plusDays(60)
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), estimatedPayoutDate)
    }
}

/**
 * Aggregated revenue analytics
 */
data class RevenueAnalytics(
    val projectId: Long? = null,
    val totalRevenue: Double = 0.0,
    val totalStreams: Long = 0,
    val platformRevenue: Map<StreamingPlatform, Double> = emptyMap(),
    val averageRevenuePerStream: Double = 0.0,
    val projectedMonthlyRevenue: Double = 0.0,
    val projectedYearlyRevenue: Double = 0.0,
    val growthRate: Float = 0f,
    val topEarningPlatform: StreamingPlatform? = null,
    val dateRange: Pair<YearMonth, YearMonth>? = null
) {
    /**
     * Calculate estimated annual revenue based on current trends
     */
    fun estimatedAnnualRevenue(): Double {
        return projectedMonthlyRevenue * 12
    }

    /**
     * Get platform with best revenue per stream
     */
    fun bestPayingPlatform(): StreamingPlatform? {
        return platformRevenue.maxByOrNull { it.value }?.key
    }

    /**
     * Calculate revenue growth percentage
     */
    fun revenueGrowthPercentage(): Float {
        return growthRate
    }

    /**
     * Check if revenue is growing
     */
    fun isGrowing(): Boolean {
        return growthRate > 0f
    }
}

/**
 * Revenue projection for future periods
 */
data class RevenueProjection(
    val period: YearMonth,
    val estimatedStreams: Long,
    val estimatedRevenue: Double,
    val confidence: Float, // 0-100 percentage
    val basedOnHistoricalData: Boolean = true
) {
    /**
     * Get confidence level
     */
    fun confidenceLevel(): ConfidenceLevel = when {
        confidence >= 80f -> ConfidenceLevel.HIGH
        confidence >= 60f -> ConfidenceLevel.MEDIUM
        confidence >= 40f -> ConfidenceLevel.LOW
        else -> ConfidenceLevel.VERY_LOW
    }
}

/**
 * Confidence level for projections
 */
enum class ConfidenceLevel(val displayName: String) {
    VERY_LOW("Very Low"),
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High")
}
