package com.example.releaseflow.core.data.local.dao

import androidx.room.*
import com.example.releaseflow.core.data.local.entity.CalendarEventEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CalendarDao {
    @Query("SELECT * FROM calendar_events ORDER BY date ASC")
    fun getAllEvents(): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE id = :id")
    fun getEventById(id: Long): Flow<CalendarEventEntity?>

    @Query("SELECT * FROM calendar_events WHERE projectId = :projectId ORDER BY date ASC")
    fun getEventsByProject(projectId: Long): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE type = :type ORDER BY date ASC")
    fun getEventsByType(type: String): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE date = :date ORDER BY time ASC")
    fun getEventsByDate(date: LocalDate): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getEventsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE date BETWEEN :today AND :futureDate ORDER BY date ASC")
    fun getUpcomingEvents(today: LocalDate, futureDate: LocalDate): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE date = :today ORDER BY time ASC")
    fun getTodayEvents(today: LocalDate): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE type IN ('RELEASE_DATE', 'UPLOAD_DEADLINE') ORDER BY date ASC")
    fun getCriticalEvents(): Flow<List<CalendarEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CalendarEventEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<CalendarEventEntity>): List<Long>

    @Update
    suspend fun updateEvent(event: CalendarEventEntity)

    @Delete
    suspend fun deleteEvent(event: CalendarEventEntity)

    @Query("DELETE FROM calendar_events WHERE projectId = :projectId")
    suspend fun deleteEventsByProject(projectId: Long)

    @Query("SELECT COUNT(*) FROM calendar_events WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getEventCountForDateRange(startDate: LocalDate, endDate: LocalDate): Int
}
