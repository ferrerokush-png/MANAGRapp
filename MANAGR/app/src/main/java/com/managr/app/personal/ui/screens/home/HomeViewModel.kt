package com.managr.app.personal.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.managr.app.core.domain.model.Project
import com.managr.app.core.domain.model.ProjectStatus
import com.managr.app.core.domain.model.Task
import com.managr.app.core.domain.repository.ProjectRepository
import com.managr.app.core.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val activeProjects: List<Project> = emptyList(),
    val pendingTasksCount: Int = 0,
    val upcomingDeadlines: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val activeProjects = projectRepository.getActiveProjects()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val pendingTasks = taskRepository.getAllTasks()
        .map { tasks -> tasks.filter { it.status != com.managr.app.core.domain.model.TaskStatus.COMPLETED } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val uiState: StateFlow<HomeUiState> = combine(
        activeProjects,
        pendingTasks
    ) { projects, tasks ->
        val upcomingDeadlines = tasks
            .filter { it.dueDate.isAfter(LocalDate.now()) }
            .sortedBy { it.dueDate }
            .take(7)

        HomeUiState(
            activeProjects = projects.take(5),
            pendingTasksCount = tasks.size,
            upcomingDeadlines = upcomingDeadlines,
            isLoading = false
        )
    }.catch { e ->
        emit(HomeUiState(error = e.message, isLoading = false))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(isLoading = true)
    )
    
    fun refreshData() {
        // Data refreshes automatically via Flow observers
    }
}
