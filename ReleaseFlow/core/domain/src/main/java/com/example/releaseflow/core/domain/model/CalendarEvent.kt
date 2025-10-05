package com.example.releaseflow.core.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Calendar event for releases, tasks, and important dates
 */
data class CalendarEvent(
    val id: Long = 0,
    val projectId: Long? = null,
    val taskId: Long? = null,
    val title: String,
    val description: String = "",
    val type: EventType,
    val date: LocalDate,
    val time: LocalDateTime? = null,
    val endDate: LocalDate? = null,
    val isAllDay: Boolean = true,
    val location: String? = null,
    val color: String? = null, // Hex color for UI
    val reminderEnabled: Boolean = true,
    val reminderMinutesBefore: Int = 1440, // Default: 1 day before
    val isRecurring: Boolean = false,
    val recurrenceRule: String? = null,
    val attendees: List<String> = emptyList(),
    val url: String? = null,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if event is today
     */
    fun isToday(): Boolean {
        return date == LocalDate.now()
    }

    /**
     * Check if event is upcoming (within next 7 days)
     */
    fun isUpcoming(): Boolean {
        val today = LocalDate.now()
        val sevenDaysFromNow = today.plusDays(7)
        return date.isAfter(today) && date.isBefore(sevenDaysFromNow)
    }

    /**
     * Check if event is in the past
     */
    fun isPast(): Boolean {
        return date.isBefore(LocalDate.now())
    }

    /**
     * Calculate days until event
     */
    fun daysUntil(): Long {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), date)
    }

    /**
     * Get event duration in days
     */
    fun durationInDays(): Long {
        endDate ?: return 1
        return java.time.temporal.ChronoUnit.DAYS.between(date, endDate) + 1
    }

    /**
     * Check if event is critical (release or upload deadline)
     */
    fun isCritical(): Boolean {
        return type in listOf(EventType.RELEASE_DATE, EventType.UPLOAD_DEADLINE)
    }

    /**
     * Get reminder date/time
     */
    fun reminderDateTime(): LocalDateTime? {
        if (!reminderEnabled) return null
        val eventDateTime = time ?: date.atStartOfDay()
        return eventDateTime.minusMinutes(reminderMinutesBefore.toLong())
    }

    /**
     * Validate event data
     */
    fun validate(): Result<Unit> {
        return when {
            title.isBlank() -> Result.failure(IllegalArgumentException("Title cannot be empty"))
            endDate != null && endDate.isBefore(date) -> Result.failure(
                IllegalArgumentException("End date cannot be before start date")
            )
            reminderMinutesBefore < 0 -> Result.failure(
                IllegalArgumentException("Reminder minutes cannot be negative")
            )
            else -> Result.success(Unit)
        }
    }
}

/**
 * Type of calendar event
 */
enum class EventType(val displayName: String, val defaultColor: String) {
    RELEASE_DATE("Release Date", "#10B981"), // Green
    UPLOAD_DEADLINE("Upload Deadline", "#EF4444"), // Red
    TASK_DEADLINE("Task Deadline", "#F59E0B"), // Orange
    PROMO_POST("Promo Post", "#8B5CF6"), // Purple
    MEETING("Meeting", "#3B82F6"), // Blue
    PERFORMANCE("Performance", "#EC4899"), // Pink
    INTERVIEW("Interview", "#06B6D4"), // Cyan
    SUBMISSION_DEADLINE("Submission Deadline", "#F59E0B"), // Orange
    MILESTONE("Milestone", "#10B981"), // Green
    OTHER("Other", "#6B7280"); // Gray

    fun isDeadline(): Boolean = this in listOf(UPLOAD_DEADLINE, TASK_DEADLINE, SUBMISSION_DEADLINE)
}

/**
 * Calendar view mode
 */
enum class CalendarViewMode(val displayName: String) {
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month"),
    AGENDA("Agenda");

    fun daysToShow(): Int = when (this) {
        DAY -> 1
        WEEK -> 7
        MONTH -> 31
        AGENDA -> 30
    }
}

/**
 * Event filter for calendar
 */
data class EventFilter(
    val projectIds: Set<Long> = emptySet(),
    val eventTypes: Set<EventType> = emptySet(),
    val dateRange: Pair<LocalDate, LocalDate>? = null,
    val showPastEvents: Boolean = false,
    val showCompletedTasks: Boolean = false
) {
    /**
     * Check if event matches filter
     */
    fun matches(event: CalendarEvent): Boolean {
        // Project filter
        if (projectIds.isNotEmpty() && event.projectId !in projectIds) return false
        
        // Event type filter
        if (eventTypes.isNotEmpty() && event.type !in eventTypes) return false
        
        // Date range filter
        dateRange?.let { (start, end) ->
            if (event.date.isBefore(start) || event.date.isAfter(end)) return false
        }
        
        // Past events filter
        if (!showPastEvents && event.isPast()) return false
        
        return true
    }

    /**
     * Check if filter is active
     */
    fun isActive(): Boolean {
        return projectIds.isNotEmpty() || 
               eventTypes.isNotEmpty() || 
               dateRange != null ||
               !showPastEvents
    }
}
