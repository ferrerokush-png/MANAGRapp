package com.example.releaseflow.core.domain.repository

import com.example.releaseflow.core.domain.model.CalendarEvent
import com.example.releaseflow.core.domain.model.EventType
import com.example.releaseflow.core.domain.model.EventFilter
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for calendar operations
 */
interface CalendarRepository {
    /**
     * Get all events
     */
    fun getAllEvents(): Flow<List<CalendarEvent>>

    /**
     * Get event by ID
     */
    fun getEventById(id: Long): Flow<CalendarEvent?>

    /**
     * Get events for a project
     */
    fun getEventsByProject(projectId: Long): Flow<List<CalendarEvent>>

    /**
     * Get events by type
     */
    fun getEventsByType(type: EventType): Flow<List<CalendarEvent>>

    /**
     * Get events for a date
     */
    fun getEventsByDate(date: LocalDate): Flow<List<CalendarEvent>>

    /**
     * Get events for date range
     */
    fun getEventsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<CalendarEvent>>

    /**
     * Get upcoming events
     */
    fun getUpcomingEvents(days: Int = 7): Flow<List<CalendarEvent>>

    /**
     * Get events for today
     */
    fun getTodayEvents(): Flow<List<CalendarEvent>>

    /**
     * Get critical events (releases, deadlines)
     */
    fun getCriticalEvents(): Flow<List<CalendarEvent>>

    /**
     * Get filtered events
     */
    fun getFilteredEvents(filter: EventFilter): Flow<List<CalendarEvent>>

    /**
     * Insert event
     */
    suspend fun insertEvent(event: CalendarEvent): Long

    /**
     * Insert multiple events
     */
    suspend fun insertEvents(events: List<CalendarEvent>): List<Long>

    /**
     * Update event
     */
    suspend fun updateEvent(event: CalendarEvent)

    /**
     * Delete event
     */
    suspend fun deleteEvent(event: CalendarEvent)

    /**
     * Delete events for a project
     */
    suspend fun deleteEventsByProject(projectId: Long)

    /**
     * Get event count for date range
     */
    suspend fun getEventCountForDateRange(startDate: LocalDate, endDate: LocalDate): Int
}
