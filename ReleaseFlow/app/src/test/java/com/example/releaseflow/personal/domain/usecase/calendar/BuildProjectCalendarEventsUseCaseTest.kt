package com.example.releaseflow.personal.domain.usecase.calendar

import com.example.releaseflow.personal.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class BuildProjectCalendarEventsUseCaseTest {

    private val computeUploadBy: ComputeUploadBy = { release, _, leadOverride ->
        release.minusDays(leadOverride ?: 21)
    }

    @Test
    fun builds_release_uploadby_task_and_post_events() {
        val project = Project(
            id = "p1",
            title = "Test Single",
            type = ReleaseType.SINGLE,
            releaseDate = LocalDate.of(2025, 10, 31),
            tasks = listOf(
                Task(
                    id = "t1",
                    projectId = "p1",
                    title = "Upload to Distributor",
                    due = LocalDate.of(2025, 10, 10)
                )
            ),
            campaigns = listOf(
                Campaign(
                    id = "c1",
                    projectId = "p1",
                    name = "Launch",
                    posts = listOf(
                        SocialPost(
                            id = "s1",
                            campaignId = "c1",
                            platform = SocialPlatform.INSTAGRAM,
                            scheduledAt = LocalDateTime.of(2025, 10, 25, 12, 0)
                        )
                    )
                )
            )
        )

        val useCase = BuildProjectCalendarEventsUseCase(computeUploadBy)
        val events = useCase(project)

        // Expect 4 events: upload-by (10/10), task due (10/10), post (10/25), release (10/31)
        assertEquals(4, events.size)
        assertEquals(EventType.UPLOAD_BY, events[0].type)
        assertEquals(LocalDate.of(2025, 10, 10), events[0].date)
        assertEquals(EventType.TASK_DUE, events[1].type)
        assertEquals(LocalDate.of(2025, 10, 10), events[1].date)
        assertEquals(EventType.CAMPAIGN_POST, events[2].type)
        assertEquals(LocalDate.of(2025, 10, 25), events[2].date)
        assertEquals(EventType.RELEASE, events[3].type)
        assertEquals(LocalDate.of(2025, 10, 31), events[3].date)
    }
}
