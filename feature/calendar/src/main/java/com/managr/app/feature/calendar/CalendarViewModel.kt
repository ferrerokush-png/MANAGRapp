package com.managr.app.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    // NOTE: Inject repositories
) : ViewModel() {

    private val _eventsForDate = MutableStateFlow<List<CalendarEvent>>(emptyList())
    val eventsForDate: StateFlow<List<CalendarEvent>> = _eventsForDate.asStateFlow()

    fun loadEventsForDate(date: LocalDate) {
        viewModelScope.launch {
            // NOTE: Load events from repository
            // For now, return empty list
            _eventsForDate.value = emptyList()
        }
    }
}
