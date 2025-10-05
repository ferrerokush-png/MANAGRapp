package com.example.releaseflow.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.releaseflow.core.domain.model.*
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Room entity for Task with foreign key to Project
 */
@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId"), Index("dueDate"), Index("status")]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val title: String,
    val description: String,
    val dueDate: LocalDate,
    val phase: String, // TaskPhase as String
    val status: String, // TaskStatus as String
    val priority: String, // TaskPriority as String
    val order: Int,
    val dependencies: List<Long>,
    val estimatedDuration: Int,
    val actualDuration: Int,
    val assignedTo: String?,
    val tags: List<String>,
    val notes: String,
    val attachments: List<String>,
    val completedAt: LocalDateTime?,
    val reminderEnabled: Boolean,
    val reminderDaysBefore: Int,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * Map TaskEntity to domain Task
 */
fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        projectId = projectId,
        title = title,
        description = description,
        dueDate = dueDate,
        phase = TaskPhase.valueOf(phase),
        status = TaskStatus.valueOf(status),
        priority = TaskPriority.valueOf(priority),
        order = order,
        dependencies = dependencies,
        estimatedDuration = estimatedDuration,
        actualDuration = actualDuration,
        assignedTo = assignedTo,
        tags = tags,
        notes = notes,
        attachments = attachments,
        completedAt = completedAt,
        reminderEnabled = reminderEnabled,
        reminderDaysBefore = reminderDaysBefore,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

/**
 * Map domain Task to TaskEntity
 */
fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        projectId = projectId,
        title = title,
        description = description,
        dueDate = dueDate,
        phase = phase.name,
        status = status.name,
        priority = priority.name,
        order = order,
        dependencies = dependencies,
        estimatedDuration = estimatedDuration,
        actualDuration = actualDuration,
        assignedTo = assignedTo,
        tags = tags,
        notes = notes,
        attachments = attachments,
        completedAt = completedAt,
        reminderEnabled = reminderEnabled,
        reminderDaysBefore = reminderDaysBefore,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
