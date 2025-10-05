package com.example.releaseflow.core.domain.usecase.calendar

import com.example.releaseflow.core.domain.model.CalendarEvent
import java.time.LocalDate
import java.time.LocalDateTime

class ScheduleNotificationUseCase {
    /**
     * Calculate when to schedule a notification for an event
     */
    operator fun invoke(event: CalendarEvent): LocalDateTime? {
        if (!event.reminderEnabled) return null
        return event.reminderDateTime()
    }
    
    /**
     * Check if notification should be scheduled
     */
    fun shouldScheduleNotification(event: CalendarEvent): Boolean {
        if (!event.reminderEnabled) return false
        val reminderTime = event.reminderDateTime() ?: return false
        return reminderTime.isAfter(LocalDateTime.now())
    }
}
