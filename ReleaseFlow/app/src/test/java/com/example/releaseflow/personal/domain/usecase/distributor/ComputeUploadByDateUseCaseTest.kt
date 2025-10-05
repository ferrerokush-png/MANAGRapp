package com.example.releaseflow.personal.domain.usecase.distributor

import com.example.releaseflow.personal.domain.model.DistributorProfile
import com.example.releaseflow.personal.domain.model.WeekendAdjust
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ComputeUploadByDateUseCaseTest {

    private val useCase = ComputeUploadByDateUseCase()

    @Test
    fun default_is_21_days_before() {
        val release = LocalDate.of(2025, 11, 21)
        val uploadBy = useCase(releaseDate = release)
        assertEquals(LocalDate.of(2025, 10, 31), uploadBy)
    }

    @Test
    fun weekend_previous_business_day_sunday() {
        // Base upload-by: 2025-03-09 (Sunday) -> previous business day is Friday 2025-03-07
        val release = LocalDate.of(2025, 3, 30) // minus 21 = 2025-03-09 (Sunday)
        val profile = DistributorProfile(id = "d1", name = "Test", weekendAdjust = WeekendAdjust.PreviousBusinessDay)
        val uploadBy = useCase(releaseDate = release, profile = profile)
        assertEquals(LocalDate.of(2025, 3, 7), uploadBy)
    }

    @Test
    fun weekend_next_business_day_saturday() {
        // Base upload-by: 2025-03-29 (Saturday) -> next business day is Monday 2025-03-31
        val release = LocalDate.of(2025, 4, 19) // minus 21 = 2025-03-29 (Saturday)
        val profile = DistributorProfile(id = "d1", name = "Test", weekendAdjust = WeekendAdjust.NextBusinessDay)
        val uploadBy = useCase(releaseDate = release, profile = profile)
        assertEquals(LocalDate.of(2025, 3, 31), uploadBy)
    }

    @Test
    fun lead_days_override_applies() {
        val release = LocalDate.of(2025, 12, 1)
        val uploadBy = useCase(releaseDate = release, leadDaysOverride = 14)
        assertEquals(LocalDate.of(2025, 11, 17), uploadBy)
    }
}

