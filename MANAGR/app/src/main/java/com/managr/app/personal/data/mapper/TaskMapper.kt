package com.managr.app.personal.data.mapper

import com.managr.app.core.domain.model.Task
import com.managr.app.core.domain.model.TaskPhase
import com.managr.app.core.domain.model.TaskPriority
import com.managr.app.core.domain.model.TaskStatus
import com.managr.app.personal.data.local.entity.ReleaseTask
import com.managr.app.personal.data.local.entity.TaskCategory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

/**
 * Mapper to convert between core domain Task and personal module ReleaseTask
 */
object TaskMapper {
    
    fun toCoreTask(releaseTask: ReleaseTask): Task {
        return Task(
            id = releaseTask.id,
            projectId = releaseTask.projectId,
            title = releaseTask.title,
            description = releaseTask.description ?: "",
            dueDate = releaseTask.dueDate.toLocalDate(),
            phase = releaseTask.category.toTaskPhase(),
            status = if (releaseTask.isCompleted) TaskStatus.COMPLETED else TaskStatus.PENDING,
            priority = releaseTask.priority.toTaskPriority(),
            assignedTo = releaseTask.assignedTo,
            createdAt = releaseTask.createdAt.time,
            updatedAt = releaseTask.createdAt.time
        )
    }
    
    fun toReleaseTask(task: Task): ReleaseTask {
        return ReleaseTask(
            id = task.id,
            projectId = task.projectId,
            title = task.title,
            description = task.description.takeIf { it.isNotEmpty() },
            dueDate = task.dueDate.toDate(),
            isCompleted = task.status == TaskStatus.COMPLETED,
            priority = task.priority.level,
            category = task.phase.toTaskCategory(),
            assignedTo = task.assignedTo,
            createdAt = Date(task.createdAt)
        )
    }
    
    private fun TaskCategory.toTaskPhase(): TaskPhase {
        return when (this) {
            TaskCategory.PRODUCTION -> TaskPhase.PRODUCTION
            TaskCategory.MARKETING -> TaskPhase.PROMOTION
            TaskCategory.DISTRIBUTION -> TaskPhase.DISTRIBUTION
        }
    }
    
    private fun TaskPhase.toTaskCategory(): TaskCategory {
        return when (this) {
            TaskPhase.PRE_PRODUCTION -> TaskCategory.PRODUCTION
            TaskPhase.PRODUCTION -> TaskCategory.PRODUCTION
            TaskPhase.DISTRIBUTION -> TaskCategory.DISTRIBUTION
            TaskPhase.PROMOTION -> TaskCategory.MARKETING
            TaskPhase.POST_RELEASE -> TaskCategory.MARKETING
        }
    }
    
    private fun Int.toTaskPriority(): TaskPriority {
        return when (this) {
            1 -> TaskPriority.LOW
            2 -> TaskPriority.MEDIUM
            3 -> TaskPriority.HIGH
            4 -> TaskPriority.CRITICAL
            else -> TaskPriority.MEDIUM
        }
    }
    
    private fun Date.toLocalDate(): LocalDate {
        return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
    
    private fun LocalDate.toDate(): Date {
        return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}
