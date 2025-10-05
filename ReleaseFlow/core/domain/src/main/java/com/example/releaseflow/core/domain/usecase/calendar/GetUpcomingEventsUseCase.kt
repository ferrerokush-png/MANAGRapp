package com.example.releaseflow.core.domain.usecase.calendar

import com.example.releaseflow.core.domain.model.CalendarEvent
import com.example.releaseflow.core.domain.repository.CalendarRepository
import kotlinx.coroutines.flow.Flow

class GetUpcomingEventsUseCase(private val calendarRepository: CalendarRepository) {
    operator fun invoke(days: Int = 7): Flow<List<CalendarEvent>> {
        return calendarRepository.getUpcomingEvents(days)
    }
}
