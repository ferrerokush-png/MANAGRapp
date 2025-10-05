package com.example.releaseflow.personal.domain.usecase.calendar

import com.example.releaseflow.personal.domain.model.*
import com.example.releaseflow.personal.domain.repository.ProjectRepository
import java.time.LocalDate

// A functional contract to compute upload-by dates; implemented by the distributor math use case.
typealias ComputeUploadBy = (releaseDate: LocalDate, profile: DistributorProfile?, leadDaysOverride: Long?) -> LocalDate

class BuildProjectCalendarEventsUseCase(
    private val computeUploadByDate: ComputeUploadBy,
) {
    operator fun invoke(project: Project, leadDaysOverride: Long? = null): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()

        // Release day
        events += CalendarEvent(
            id = "rel-${project.id}",
            projectId = project.id,
            date = project.releaseDate,
            type = EventType.RELEASE,
            title = "Release Day — ${project.title}",
            description = "${project.type}",
        )

        // Upload-by date (distributor policy)
        val uploadBy = computeUploadByDate(
            project.releaseDate,
            project.distributorProfile,
            leadDaysOverride
        )
        events += CalendarEvent(
            id = "up-${project.id}",
            projectId = project.id,
            date = uploadBy,
            type = EventType.UPLOAD_BY,
            title = "Upload to Distributor — ${project.title}",
        )

        // Task due dates
        project.tasks.forEach { task ->
            events += CalendarEvent(
                id = "tsk-${task.id}",
                projectId = project.id,
                date = task.due,
                type = EventType.TASK_DUE,
                title = task.title,
                refId = task.id,
            )
        }

        // Campaign posts (convert LocalDateTime -> LocalDate)
        project.campaigns.flatMap { it.posts }.forEach { post ->
            val date = post.scheduledAt?.toLocalDate() ?: return@forEach
            events += CalendarEvent(
                id = "pst-${post.id}",
                projectId = project.id,
                date = date,
                type = EventType.CAMPAIGN_POST,
                title = "Post: ${post.platform}",
                refId = post.id,
            )
        }

        return events.sortedBy { it.date }
    }
}

class ListCalendarEventsInRangeUseCase(
    private val projectRepository: ProjectRepository,
    private val buildProjectEvents: BuildProjectCalendarEventsUseCase,
) {
    suspend operator fun invoke(start: LocalDate, end: LocalDate): List<CalendarEvent> {
        val projects = projectRepository.listProjects()
        val all = projects.flatMap { buildProjectEvents(it) }
        return all.filter { it.date >= start && it.date <= end }.sortedBy { it.date }
    }
}
