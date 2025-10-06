package com.managr.app.core.domain.usecase.analytics

import com.managr.app.core.domain.model.SocialMediaMetrics
import com.managr.app.core.domain.repository.SocialMediaRepository

class RecordSocialMediaDataUseCase(private val socialMediaRepository: SocialMediaRepository) {
    suspend operator fun invoke(metrics: SocialMediaMetrics): Result<Long> {
        return try {
            val id = socialMediaRepository.insertSocialMediaMetrics(metrics)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
