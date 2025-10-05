package com.example.releaseflow.core.domain.model

/**
 * Task completion status
 */
enum class TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    OVERDUE,
    CANCELLED;

    /**
     * Check if task is actionable
     */
    fun isActionable(): Boolean = this == PENDING || this == IN_PROGRESS || this == OVERDUE

    /**
     * Check if task is done
     */
    fun isDone(): Boolean = this == COMPLETED || this == CANCELLED

    /**
     * Get display color hint
     */
    fun colorHint(): String = when (this) {
        PENDING -> "gray"
        IN_PROGRESS -> "blue"
        COMPLETED -> "green"
        OVERDUE -> "red"
        CANCELLED -> "gray"
    }
}
