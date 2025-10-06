package com.managr.app.feature.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.managr.app.core.domain.model.Project
import com.managr.app.core.domain.model.ProjectStatus
import com.managr.app.core.domain.model.ReleaseType
import com.managr.app.core.domain.usecase.project.DeleteProjectUseCase
import com.managr.app.core.domain.usecase.project.GetProjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Projects list screen
 */
@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow<ProjectsUiState>(ProjectsUiState.Loading)
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Filters
    private val _selectedType = MutableStateFlow<ReleaseType?>(null)
    val selectedType: StateFlow<ReleaseType?> = _selectedType.asStateFlow()

    private val _selectedStatus = MutableStateFlow<ProjectStatus?>(null)
    val selectedStatus: StateFlow<ProjectStatus?> = _selectedStatus.asStateFlow()

    // Refresh state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadProjects()
    }

    /**
     * Load projects with filters applied
     */
    fun loadProjects() {
        viewModelScope.launch {
            try {
                _uiState.value = ProjectsUiState.Loading

                combine(
                    getProjectsFlow(),
                    _searchQuery,
                    _selectedType,
                    _selectedStatus
                ) { projects, query, type, status ->
                    filterProjects(projects, query, type, status)
                }.catch { error ->
                    _uiState.value = ProjectsUiState.Error(error.message ?: "Failed to load projects")
                }.collect { filteredProjects ->
                    _uiState.value = if (filteredProjects.isEmpty()) {
                        ProjectsUiState.Empty
                    } else {
                        ProjectsUiState.Success(filteredProjects)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ProjectsUiState.Error(e.message ?: "Failed to load projects")
            }
        }
    }

    /**
     * Get projects flow based on current filters
     */
    private fun getProjectsFlow(): Flow<List<Project>> {
        return when {
            _selectedStatus.value != null -> getProjectsUseCase.getProjectsByStatus(_selectedStatus.value!!)
            _selectedType.value != null -> getProjectsUseCase.getProjectsByType(_selectedType.value!!)
            else -> getProjectsUseCase.getAllProjects()
        }
    }

    /**
     * Filter projects by search query
     */
    private fun filterProjects(
        projects: List<Project>,
        query: String,
        type: ReleaseType?,
        status: ProjectStatus?
    ): List<Project> {
        var filtered = projects

        // Apply type filter
        if (type != null) {
            filtered = filtered.filter { it.type == type }
        }

        // Apply status filter
        if (status != null) {
            filtered = filtered.filter { it.status == status }
        }

        // Apply search query
        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.artistName.contains(query, ignoreCase = true) ||
                it.genre?.contains(query, ignoreCase = true) == true
            }
        }

        return filtered
    }

    /**
     * Update search query
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /**
     * Update type filter
     */
    fun onTypeFilterChange(type: ReleaseType?) {
        _selectedType.value = type
    }

    /**
     * Update status filter
     */
    fun onStatusFilterChange(status: ProjectStatus?) {
        _selectedStatus.value = status
    }

    /**
     * Clear all filters
     */
    fun clearFilters() {
        _searchQuery.value = ""
        _selectedType.value = null
        _selectedStatus.value = null
    }

    /**
     * Refresh projects (pull-to-refresh)
     */
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                // Reload projects
                loadProjects()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    /**
     * Delete a project
     */
    fun deleteProject(project: Project) {
        viewModelScope.launch {
            deleteProjectUseCase(project).onFailure { error ->
                _uiState.value = ProjectsUiState.Error(error.message ?: "Failed to delete project")
            }
        }
    }
}

/**
 * UI State for Projects screen
 */
sealed class ProjectsUiState {
    object Loading : ProjectsUiState()
    data class Success(val projects: List<Project>) : ProjectsUiState()
    object Empty : ProjectsUiState()
    data class Error(val message: String) : ProjectsUiState()
}
