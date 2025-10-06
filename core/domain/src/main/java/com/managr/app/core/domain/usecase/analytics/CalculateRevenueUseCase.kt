package com.managr.app.core.domain.usecase.analytics

import com.managr.app.core.domain.model.Revenue
import com.managr.app.core.domain.model.StreamingPlatform
import com.managr.app.core.domain.repository.RevenueRepository
import com.managr.app.core.domain.util.Constants

class CalculateRevenueUseCase(private val revenueRepository: RevenueRepository) {
    /**
     * Calculate estimated revenue from streams
     */
    fun calculateEstimatedRevenue(platform: StreamingPlatform, streams: Long): Double {
        val ratePerStream = when (platform) {
            StreamingPlatform.SPOTIFY -> Constants.Revenue.SPOTIFY_AVG_PER_STREAM
            StreamingPlatform.APPLE_MUSIC -> Constants.Revenue.APPLE_MUSIC_AVG_PER_STREAM
            StreamingPlatform.YOUTUBE_MUSIC -> Constants.Revenue.YOUTUBE_AVG_PER_STREAM
            StreamingPlatform.TIDAL -> Constants.Revenue.TIDAL_AVG_PER_STREAM
            else -> 0.005 // Default average
        }
        return streams * ratePerStream
    }
    
    /**
     * Record revenue
     */
    suspend fun recordRevenue(revenue: Revenue): Result<Long> {
        return try {
            val id = revenueRepository.insertRevenue(revenue)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
