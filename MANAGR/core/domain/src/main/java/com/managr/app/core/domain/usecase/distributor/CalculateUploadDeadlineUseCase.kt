package com.managr.app.core.domain.usecase.distributor

import com.managr.app.core.domain.model.Distributor
import com.managr.app.core.domain.model.DistributorType
import com.managr.app.core.domain.util.Constants
import java.time.LocalDate

/**
 * Use case for calculating upload deadlines
 * Default: 21 days before release for playlist pitching
 */
class CalculateUploadDeadlineUseCase {
    /**
     * Calculate upload deadline based on distributor and release date
     * 
     * @param releaseDate The release date
     * @param distributorType The distributor type
     * @return Upload deadline date
     */
    operator fun invoke(releaseDate: LocalDate, distributorType: DistributorType): LocalDate {
        return Distributor.calculateUploadDeadline(releaseDate, distributorType)
    }
    
    /**
     * Calculate upload deadline with custom days
     */
    fun calculateWithCustomDays(releaseDate: LocalDate, daysBefore: Int): LocalDate {
        return releaseDate.minusDays(daysBefore.toLong())
    }
    
    /**
     * Get default upload days for distributor
     */
    fun getDefaultUploadDays(distributorType: DistributorType): Int {
        return distributorType.minUploadDays
    }
    
    /**
     * Check if upload deadline is approaching (within 7 days)
     */
    fun isDeadlineApproaching(uploadDeadline: LocalDate): Boolean {
        val daysUntil = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), uploadDeadline)
        return daysUntil in 1..Constants.Distributor.DEADLINE_APPROACHING_DAYS
    }
    
    /**
     * Check if upload deadline is overdue
     */
    fun isDeadlineOverdue(uploadDeadline: LocalDate): Boolean {
        return LocalDate.now().isAfter(uploadDeadline)
    }
}
