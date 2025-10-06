package com.managr.app.personal.domain.usecase.calendar

import com.managr.app.core.domain.model.Project
import com.managr.app.core.domain.model.CalendarEvent
import com.managr.app.core.domain.model.EventType
import com.managr.app.core.domain.repository.ProjectRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.first

// A functional contract to compute upload-by dates; implemented by the distributor math use case.
typealias ComputeUploadBy = (releaseDate: LocalDate, profile: String?, leadDaysOverride: Long?) -> LocalDate

class BuildProjectCalendarEventsUseCase(
    private val computeUploadByDate: ComputeUploadBy,
) {
    operator fun invoke(project: Project, leadDaysOverride: Long? = null): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()

        // Release day
        events += CalendarEvent(
            id = project.id,
            projectId = project.id,
            title = "Release Day — ${project.title}",
            description = "${project.type}",
            type = EventType.RELEASE_DATE,
            date = project.releaseDate
        )

        // Upload-by date (distributor policy)
        val uploadBy = computeUploadByDate(
            project.releaseDate,
            project.distributorType.name,
            leadDaysOverride
        )
        events += CalendarEvent(
            id = project.id + 1000, // Different ID to avoid conflicts
            projectId = project.id,
            title = "Upload to Distributor — ${project.title}",
            type = EventType.UPLOAD_DEADLINE,
            date = uploadBy
        )

        return events.sortedBy { it.date }
    }
}

class ListCalendarEventsInRangeUseCase(
    private val projectRepository: ProjectRepository,
    private val buildProjectEvents: BuildProjectCalendarEventsUseCase,
) {
    suspend operator fun invoke(start: LocalDate, end: LocalDate): List<CalendarEvent> {
        val projects = projectRepository.getAllProjects().first()
        val all = projects.flatMap { buildProjectEvents(it) }
        return all.filter { it.date >= start && it.date <= end }.sortedBy { it.date }
    }
}
