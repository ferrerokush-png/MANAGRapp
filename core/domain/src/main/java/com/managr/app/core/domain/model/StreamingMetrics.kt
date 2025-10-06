package com.managr.app.core.domain.model

import java.time.LocalDate

/**
 * Streaming metrics for a specific platform and project/track
 */
data class StreamingMetrics(
    val id: Long = 0,
    val projectId: Long? = null, // null means overall artist metrics
    val trackId: Long? = null, // null means project-level metrics
    val platform: StreamingPlatform,
    val date: LocalDate,
    val streams: Long = 0,
    val listeners: Long = 0,
    val saves: Long = 0,
    val playlistAdds: Long = 0,
    val skipRate: Float = 0f, // Percentage (0-100)
    val completionRate: Float = 0f, // Percentage (0-100)
    val averageListenDuration: Int = 0, // in seconds
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Calculate engagement score (0-100)
     */
    fun engagementScore(): Float {
        val saveRate = if (listeners > 0) (saves.toFloat() / listeners) * 100 else 0f
        val playlistRate = if (listeners > 0) (playlistAdds.toFloat() / listeners) * 100 else 0f
        return (completionRate * 0.4f + saveRate * 0.3f + playlistRate * 0.3f).coerceIn(0f, 100f)
    }

    /**
     * Calculate streams per listener ratio
     */
    fun streamsPerListener(): Float {
        return if (listeners > 0) streams.toFloat() / listeners else 0f
    }

    /**
     * Check if metrics are performing well
     */
    fun isPerformingWell(): Boolean {
        return completionRate >= 50f && skipRate <= 30f && streamsPerListener() >= 1.5f
    }
}

/**
 * Aggregated streaming analytics across all platforms
 */
data class StreamingAnalytics(
    val projectId: Long? = null,
    val totalStreams: Long = 0,
    val totalListeners: Long = 0,
    val totalSaves: Long = 0,
    val totalPlaylistAdds: Long = 0,
    val platformMetrics: Map<StreamingPlatform, StreamingMetrics> = emptyMap(),
    val growthRate: Float = 0f, // Percentage change from previous period
    val topPlatform: StreamingPlatform? = null,
    val dateRange: Pair<LocalDate, LocalDate>? = null
) {
    /**
     * Calculate overall engagement score
     */
    fun overallEngagementScore(): Float {
        if (platformMetrics.isEmpty()) return 0f
        return platformMetrics.values.map { it.engagementScore() }.average().toFloat()
    }

    /**
     * Get platform with highest growth
     */
    fun fastestGrowingPlatform(): StreamingPlatform? {
        return platformMetrics.maxByOrNull { it.value.growthRate() }?.key
    }

    /**
     * Calculate average streams per day
     */
    fun averageStreamsPerDay(): Long {
        val (start, end) = dateRange ?: return 0
        val days = java.time.temporal.ChronoUnit.DAYS.between(start, end).coerceAtLeast(1)
        return totalStreams / days
    }
}

/**
 * Helper extension for growth rate calculation
 */
private fun StreamingMetrics.growthRate(): Float = 0f // Placeholder for actual calculation
