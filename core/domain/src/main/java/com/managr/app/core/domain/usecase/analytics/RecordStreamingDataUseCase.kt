package com.managr.app.core.domain.usecase.analytics

import com.managr.app.core.domain.model.StreamingMetrics
import com.managr.app.core.domain.repository.AnalyticsRepository
import com.managr.app.core.domain.util.ValidationRules

class RecordStreamingDataUseCase(private val analyticsRepository: AnalyticsRepository) {
    suspend operator fun invoke(metrics: StreamingMetrics): Result<Long> {
        return try {
            if (!ValidationRules.isValidStreamsCount(metrics.streams)) {
                return Result.failure(IllegalArgumentException("Invalid streams count"))
            }
            val id = analyticsRepository.insertStreamingMetrics(metrics)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
