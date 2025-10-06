package com.managr.app.personal.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.managr.app.personal.data.local.dao.ProjectDao
import com.managr.app.personal.data.local.entity.ReleaseProject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class CalendarViewModel @Inject constructor(
    projectDao: ProjectDao
) : ViewModel() {

    private val zoneId: ZoneId = ZoneId.systemDefault()

    // All projects grouped by release LocalDate
    val projectsByDate: StateFlow<Map<LocalDate, List<ReleaseProject>>> =
        projectDao.getAllProjects()
            .map { projects ->
                projects.groupBy { p ->
                    Instant.ofEpochMilli(p.releaseDate.time).atZone(zoneId).toLocalDate()
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyMap()
            )

    // Selected date in the calendar
    private val _selectedDate = MutableStateFlow(LocalDate.now(zoneId))
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    fun setSelectedDate(date: LocalDate) { _selectedDate.value = date }

    // Projects on the selected date
    val selectedDateProjects: StateFlow<List<ReleaseProject>> =
        combine(projectsByDate, selectedDate) { map, date ->
            map[date].orEmpty().sortedBy { it.title }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )
}

