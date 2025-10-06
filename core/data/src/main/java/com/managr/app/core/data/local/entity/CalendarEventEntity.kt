package com.managr.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.managr.app.core.domain.model.CalendarEvent
import com.managr.app.core.domain.model.EventType
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "calendar_events",
    indices = [Index("projectId"), Index("date"), Index("type")]
)
data class CalendarEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long?,
    val taskId: Long?,
    val title: String,
    val description: String,
    val type: String, // EventType as String
    val date: LocalDate,
    val time: LocalDateTime?,
    val endDate: LocalDate?,
    val isAllDay: Boolean,
    val location: String?,
    val color: String?,
    val reminderEnabled: Boolean,
    val reminderMinutesBefore: Int,
    val isRecurring: Boolean,
    val recurrenceRule: String?,
    val attendees: List<String>,
    val url: String?,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)

fun CalendarEventEntity.toDomain(): CalendarEvent {
    return CalendarEvent(
        id = id,
        projectId = projectId,
        taskId = taskId,
        title = title,
        description = description,
        type = EventType.valueOf(type),
        date = date,
        time = time,
        endDate = endDate,
        isAllDay = isAllDay,
        location = location,
        color = color,
        reminderEnabled = reminderEnabled,
        reminderMinutesBefore = reminderMinutesBefore,
        isRecurring = isRecurring,
        recurrenceRule = recurrenceRule,
        attendees = attendees,
        url = url,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CalendarEvent.toEntity(): CalendarEventEntity {
    return CalendarEventEntity(
        id = id,
        projectId = projectId,
        taskId = taskId,
        title = title,
        description = description,
        type = type.name,
        date = date,
        time = time,
        endDate = endDate,
        isAllDay = isAllDay,
        location = location,
        color = color,
        reminderEnabled = reminderEnabled,
        reminderMinutesBefore = reminderMinutesBefore,
        isRecurring = isRecurring,
        recurrenceRule = recurrenceRule,
        attendees = attendees,
        url = url,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
