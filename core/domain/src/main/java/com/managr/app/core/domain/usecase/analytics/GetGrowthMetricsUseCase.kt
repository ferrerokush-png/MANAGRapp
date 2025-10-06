package com.managr.app.core.domain.usecase.analytics

import com.managr.app.core.domain.repository.AnalyticsRepository
import com.managr.app.core.domain.repository.SocialMediaRepository
import java.time.LocalDate

class GetGrowthMetricsUseCase(
    private val analyticsRepository: AnalyticsRepository,
    private val socialMediaRepository: SocialMediaRepository
) {
    /**
     * Calculate streaming growth rate
     */
    suspend fun calculateStreamingGrowth(projectId: Long, days: Int = 30): Result<Float> {
        return try {
            val endDate = LocalDate.now()
            val startDate = endDate.minusDays(days.toLong())
            val midDate = startDate.plusDays((days / 2).toLong())
            
            val firstHalfStreams = analyticsRepository.getTotalStreamsForDateRange(startDate, midDate)
            val secondHalfStreams = analyticsRepository.getTotalStreamsForDateRange(midDate, endDate)
            
            if (firstHalfStreams == 0L) {
                return Result.success(0f)
            }
            
            val growthRate = ((secondHalfStreams - firstHalfStreams).toFloat() / firstHalfStreams) * 100
            Result.success(growthRate)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
