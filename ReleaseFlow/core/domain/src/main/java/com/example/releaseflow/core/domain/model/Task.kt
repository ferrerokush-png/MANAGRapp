package com.example.releaseflow.core.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Domain model for a project task
 * Represents a single task in the release workflow with dependencies and tracking
 */
data class Task(
    val id: Long = 0,
    val projectId: Long,
    val title: String,
    val description: String = "",
    val dueDate: LocalDate,
    val phase: TaskPhase = TaskPhase.PRE_PRODUCTION,
    val status: TaskStatus = TaskStatus.PENDING,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val order: Int = 0,
    val dependencies: List<Long> = emptyList(), // IDs of tasks that must be completed first
    val estimatedDuration: Int = 0, // in minutes
    val actualDuration: Int = 0, // in minutes
    val assignedTo: String? = null, // For team collaboration
    val tags: List<String> = emptyList(),
    val notes: String = "",
    val attachments: List<String> = emptyList(), // URIs to files
    val completedAt: LocalDateTime? = null,
    val reminderEnabled: Boolean = true,
    val reminderDaysBefore: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if task is overdue
     */
    fun isOverdue(): Boolean {
        val today = LocalDate.now()
        return dueDate.isBefore(today) && status != TaskStatus.COMPLETED && status != TaskStatus.CANCELLED
    }

    /**
     * Check if task is due soon (within 3 days)
     */
    fun isDueSoon(): Boolean {
        val today = LocalDate.now()
        val threeDaysFromNow = today.plusDays(3)
        return dueDate.isAfter(today) && dueDate.isBefore(threeDaysFromNow) && status.isActionable()
    }

    /**
     * Calculate days until due
     */
    fun daysUntilDue(): Long {
        val today = LocalDate.now()
        return java.time.temporal.ChronoUnit.DAYS.between(today, dueDate)
    }

    /**
     * Check if task can be started (all dependencies completed)
     */
    fun canStart(completedTaskIds: Set<Long>): Boolean {
        return dependencies.all { it in completedTaskIds }
    }

    /**
     * Get urgency level
     */
    fun urgencyLevel(): UrgencyLevel = when {
        isOverdue() -> UrgencyLevel.CRITICAL
        isDueSoon() && priority == TaskPriority.HIGH -> UrgencyLevel.HIGH
        isDueSoon() -> UrgencyLevel.MEDIUM
        else -> UrgencyLevel.LOW
    }

    /**
     * Validate task data
     */
    fun validate(): Result<Unit> {
        return when {
            title.isBlank() -> Result.failure(IllegalArgumentException("Title cannot be empty"))
            estimatedDuration < 0 -> Result.failure(IllegalArgumentException("Duration cannot be negative"))
            reminderDaysBefore < 0 -> Result.failure(IllegalArgumentException("Reminder days cannot be negative"))
            else -> Result.success(Unit)
        }
    }
}

/**
 * Task priority level
 */
enum class TaskPriority(val displayName: String, val level: Int) {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    CRITICAL("Critical", 4);

    fun isHighPriority(): Boolean = this in listOf(HIGH, CRITICAL)
}

/**
 * Task urgency level (calculated from priority and due date)
 */
enum class UrgencyLevel(val displayName: String, val colorHint: String) {
    LOW("Low", "green"),
    MEDIUM("Medium", "yellow"),
    HIGH("High", "orange"),
    CRITICAL("Critical", "red")
}
