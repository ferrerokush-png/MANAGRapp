package com.managr.app.core.domain.usecase.calendar

import com.managr.app.core.domain.model.CalendarEvent
import com.managr.app.core.domain.repository.CalendarRepository
import kotlinx.coroutines.flow.Flow

class GetUpcomingEventsUseCase(private val calendarRepository: CalendarRepository) {
    operator fun invoke(days: Int = 7): Flow<List<CalendarEvent>> {
        return calendarRepository.getUpcomingEvents(days)
    }
}
