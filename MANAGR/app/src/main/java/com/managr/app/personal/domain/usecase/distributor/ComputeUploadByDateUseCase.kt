package com.managr.app.personal.domain.usecase.distributor

import com.managr.app.personal.domain.model.DistributorProfile
import com.managr.app.personal.domain.model.WeekendAdjust
import java.time.DayOfWeek
import java.time.LocalDate

class ComputeUploadByDateUseCase {
    operator fun invoke(
        releaseDate: LocalDate,
        profile: DistributorProfile? = null,
        leadDaysOverride: Long? = null,
    ): LocalDate {
        val baseLead = leadDaysOverride ?: profile?.defaultLeadDays ?: 21L
        var uploadBy = releaseDate.minusDays(baseLead)

        val adjust = profile?.weekendAdjust ?: WeekendAdjust.None
        if (adjust != WeekendAdjust.None) {
            uploadBy = when (uploadBy.dayOfWeek) {
                DayOfWeek.SATURDAY -> when (adjust) {
                    WeekendAdjust.PreviousBusinessDay -> uploadBy.minusDays(1)
                    WeekendAdjust.NextBusinessDay -> uploadBy.plusDays(2)
                    else -> uploadBy
                }
                DayOfWeek.SUNDAY -> when (adjust) {
                    WeekendAdjust.PreviousBusinessDay -> uploadBy.minusDays(2)
                    WeekendAdjust.NextBusinessDay -> uploadBy.plusDays(1)
                    else -> uploadBy
                }
                else -> uploadBy
            }
        }
        return uploadBy
    }
}

