package com.example.releaseflow.core.domain.model

import java.time.LocalDate

/**
 * Social media metrics for a specific platform
 */
data class SocialMediaMetrics(
    val id: Long = 0,
    val projectId: Long? = null, // null means overall artist metrics
    val platform: SocialPlatform,
    val date: LocalDate,
    val followers: Long = 0,
    val posts: Int = 0,
    val views: Long = 0,
    val likes: Long = 0,
    val comments: Long = 0,
    val shares: Long = 0,
    val reach: Long = 0,
    val impressions: Long = 0,
    val profileVisits: Long = 0,
    val linkClicks: Long = 0,
    val videoViews: Long = 0,
    val averageWatchTime: Int = 0, // in seconds
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Calculate engagement rate (percentage)
     */
    fun engagementRate(): Float {
        if (followers == 0L) return 0f
        val totalEngagements = likes + comments + shares
        return (totalEngagements.toFloat() / followers) * 100
    }

    /**
     * Calculate average engagement per post
     */
    fun averageEngagementPerPost(): Float {
        if (posts == 0) return 0f
        val totalEngagements = likes + comments + shares
        return totalEngagements.toFloat() / posts
    }

    /**
     * Calculate viral potential score (0-100)
     */
    fun viralPotential(): Float {
        val shareRate = if (views > 0) (shares.toFloat() / views) * 100 else 0f
        val engagementRate = engagementRate()
        val reachRate = if (followers > 0) (reach.toFloat() / followers) * 100 else 0f
        
        return ((shareRate * 0.4f) + (engagementRate * 0.3f) + (reachRate * 0.3f)).coerceIn(0f, 100f)
    }

    /**
     * Calculate click-through rate (percentage)
     */
    fun clickThroughRate(): Float {
        if (impressions == 0L) return 0f
        return (linkClicks.toFloat() / impressions) * 100
    }

    /**
     * Check if content is performing well
     */
    fun isPerformingWell(): Boolean {
        return engagementRate() >= 3f && viralPotential() >= 40f
    }

    /**
     * Get performance tier
     */
    fun performanceTier(): PerformanceTier = when {
        engagementRate() >= 10f -> PerformanceTier.EXCELLENT
        engagementRate() >= 5f -> PerformanceTier.GOOD
        engagementRate() >= 2f -> PerformanceTier.AVERAGE
        else -> PerformanceTier.NEEDS_IMPROVEMENT
    }
}

/**
 * Aggregated social media analytics across all platforms
 */
data class SocialMediaAnalytics(
    val projectId: Long? = null,
    val totalFollowers: Long = 0,
    val totalViews: Long = 0,
    val totalEngagements: Long = 0,
    val platformMetrics: Map<SocialPlatform, SocialMediaMetrics> = emptyMap(),
    val growthRate: Float = 0f,
    val topPlatform: SocialPlatform? = null,
    val dateRange: Pair<LocalDate, LocalDate>? = null
) {
    /**
     * Calculate overall engagement rate
     */
    fun overallEngagementRate(): Float {
        if (totalFollowers == 0L) return 0f
        return (totalEngagements.toFloat() / totalFollowers) * 100
    }

    /**
     * Get platform with highest engagement
     */
    fun mostEngagedPlatform(): SocialPlatform? {
        return platformMetrics.maxByOrNull { it.value.engagementRate() }?.key
    }

    /**
     * Get platform with highest viral potential
     */
    fun mostViralPlatform(): SocialPlatform? {
        return platformMetrics.maxByOrNull { it.value.viralPotential() }?.key
    }
}

/**
 * Performance tier for social media content
 */
enum class PerformanceTier(val displayName: String, val colorHint: String) {
    EXCELLENT("Excellent", "green"),
    GOOD("Good", "blue"),
    AVERAGE("Average", "yellow"),
    NEEDS_IMPROVEMENT("Needs Improvement", "red")
}
