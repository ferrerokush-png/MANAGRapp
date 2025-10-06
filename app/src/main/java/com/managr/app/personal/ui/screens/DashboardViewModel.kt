package com.managr.app.personal.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.managr.app.personal.data.local.dao.ProjectDao
import com.managr.app.personal.data.local.dao.TaskDao
import com.managr.app.personal.data.local.entity.ProjectStatus
import com.managr.app.personal.data.local.entity.ReleaseProject
import com.managr.app.personal.data.local.entity.ReleaseTask
import com.managr.app.personal.data.local.entity.ReleaseType
import com.managr.app.personal.data.templates.ProjectTemplate
import com.managr.app.personal.data.templates.TemplateService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(
        val activeProjects: List<ReleaseProject>,
        val pendingTasks: Int,
        val upcomingDeadlines: List<ReleaseTask>
    ) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
    private val templateService: TemplateService
) : ViewModel() {

    val projects: StateFlow<List<ReleaseProject>> =
        projectDao.getAllProjects()
            .map { it.sortedBy { p -> p.releaseDate } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val activeProjects: StateFlow<List<ReleaseProject>> =
        projectDao.getProjectsByStatus(ProjectStatus.ACTIVE.name)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val allTasks: StateFlow<List<ReleaseTask>> =
        taskDao.getAllTasks()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val pendingTasks: StateFlow<List<ReleaseTask>> =
        taskDao.getTasksByCompletion(false)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val dashboardState: StateFlow<DashboardUiState> = combine(
        activeProjects,
        pendingTasks
    ) { active, pending ->
        val upcoming = pending
            .filter { it.dueDate.after(Date()) }
            .sortedBy { it.dueDate }
            .take(7)
        
        DashboardUiState.Success(
            activeProjects = active,
            pendingTasks = pending.size,
            upcomingDeadlines = upcoming
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState.Loading
    )

    fun addProject(
        title: String,
        artistName: String,
        releaseType: ReleaseType,
        releaseDate: Date,
        genre: String? = null
    ) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val projectId = projectDao.insertProject(
                ReleaseProject(
                    title = title.trim(),
                    artistName = artistName.trim(),
                    releaseType = releaseType,
                    releaseDate = releaseDate,
                    genre = genre,
                    status = ProjectStatus.ACTIVE
                )
            )

            // Generate and insert tasks from template
            val template = when (releaseType) {
                ReleaseType.SINGLE -> ProjectTemplate.SINGLE
                ReleaseType.EP -> ProjectTemplate.EP
                ReleaseType.ALBUM -> ProjectTemplate.ALBUM
            }
            
            val tasks = templateService.generateTasksForTemplate(projectId, template, releaseDate)
            tasks.forEach { task ->
                taskDao.insertTask(task)
            }
        }
    }

    fun refreshData() {
        // Data automatically refreshes via Flow
    }
}
