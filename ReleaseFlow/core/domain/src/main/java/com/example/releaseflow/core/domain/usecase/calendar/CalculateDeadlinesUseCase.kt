package com.example.releaseflow.core.domain.usecase.calendar

import com.example.releaseflow.core.domain.model.CalendarEvent
import com.example.releaseflow.core.domain.model.EventType
import com.example.releaseflow.core.domain.model.Project
import com.example.releaseflow.core.domain.model.Task
import java.time.LocalDate

class CalculateDeadlinesUseCase {
    /**
     * Calculate all deadlines for a project
     */
    operator fun invoke(project: Project, tasks: List<Task>): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()
        
        // Release date
        events.add(
            CalendarEvent(
                projectId = project.id,
                title = "Release: ${project.title}",
                description = "${project.type.displayName()} release",
                type = EventType.RELEASE_DATE,
                date = project.releaseDate,
                color = EventType.RELEASE_DATE.defaultColor
            )
        )
        
        // Upload deadline
        project.uploadDeadline?.let { deadline ->
            events.add(
                CalendarEvent(
                    projectId = project.id,
                    title = "Upload Deadline: ${project.title}",
                    description = "Upload to ${project.distributorType.displayName}",
                    type = EventType.UPLOAD_DEADLINE,
                    date = deadline,
                    color = EventType.UPLOAD_DEADLINE.defaultColor
                )
            )
        }
        
        // Task deadlines
        tasks.forEach { task ->
            events.add(
                CalendarEvent(
                    projectId = project.id,
                    taskId = task.id,
                    title = task.title,
                    description = task.description,
                    type = EventType.TASK_DEADLINE,
                    date = task.dueDate,
                    color = EventType.TASK_DEADLINE.defaultColor
                )
            )
        }
        
        return events.sortedBy { it.date }
    }
}
