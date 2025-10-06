package com.managr.app.core.data.repository

import com.managr.app.core.data.local.dao.CalendarDao
import com.managr.app.core.data.local.entity.toEntity
import com.managr.app.core.data.local.entity.toDomain
import com.managr.app.core.domain.model.*
import com.managr.app.core.domain.repository.CalendarRepository
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarDao: CalendarDao
) : CalendarRepository {

    override fun getAllEvents(): Flow<List<CalendarEvent>> =
        calendarDao.getAllEvents().map { it.map { e -> e.toDomain() } }

    override fun getEventById(id: Long): Flow<CalendarEvent?> =
        calendarDao.getEventById(id).map { it?.toDomain() }

    override fun getEventsByProject(projectId: Long): Flow<List<CalendarEvent>> =
        calendarDao.getEventsByProject(projectId).map { it.map { e -> e.toDomain() } }

    override fun getEventsByType(type: EventType): Flow<List<CalendarEvent>> =
        calendarDao.getEventsByType(type.name).map { it.map { e -> e.toDomain() } }

    override fun getEventsByDate(date: LocalDate): Flow<List<CalendarEvent>> =
        calendarDao.getEventsByDate(date).map { it.map { e -> e.toDomain() } }

    override fun getEventsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<CalendarEvent>> =
        calendarDao.getEventsByDateRange(startDate, endDate).map { it.map { e -> e.toDomain() } }

    override fun getUpcomingEvents(days: Int): Flow<List<CalendarEvent>> {
        val today = LocalDate.now()
        val futureDate = today.plusDays(days.toLong())
        return calendarDao.getUpcomingEvents(today, futureDate).map { it.map { e -> e.toDomain() } }
    }

    override fun getTodayEvents(): Flow<List<CalendarEvent>> =
        calendarDao.getTodayEvents(LocalDate.now()).map { it.map { e -> e.toDomain() } }

    override fun getCriticalEvents(): Flow<List<CalendarEvent>> =
        calendarDao.getCriticalEvents().map { it.map { e -> e.toDomain() } }

    override fun getFilteredEvents(filter: EventFilter): Flow<List<CalendarEvent>> =
        getAllEvents().map { events -> events.filter { filter.matches(it) } }

    override suspend fun insertEvent(event: CalendarEvent): Long =
        calendarDao.insertEvent(event.toEntity())

    override suspend fun insertEvents(events: List<CalendarEvent>): List<Long> =
        calendarDao.insertEvents(events.map { it.toEntity() })

    override suspend fun updateEvent(event: CalendarEvent) =
        calendarDao.updateEvent(event.toEntity())

    override suspend fun deleteEvent(event: CalendarEvent) =
        calendarDao.deleteEvent(event.toEntity())

    override suspend fun deleteEventsByProject(projectId: Long) =
        calendarDao.deleteEventsByProject(projectId)

    override suspend fun getEventCountForDateRange(startDate: LocalDate, endDate: LocalDate): Int =
        calendarDao.getEventCountForDateRange(startDate, endDate)
}
