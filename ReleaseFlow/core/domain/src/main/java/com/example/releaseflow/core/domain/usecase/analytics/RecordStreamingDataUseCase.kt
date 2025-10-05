package com.example.releaseflow.core.domain.usecase.analytics

import com.example.releaseflow.core.domain.model.StreamingMetrics
import com.example.releaseflow.core.domain.repository.AnalyticsRepository
import com.example.releaseflow.core.domain.util.ValidationRules

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
