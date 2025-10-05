package com.example.releaseflow.feature.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.releaseflow.core.domain.model.Project
import com.example.releaseflow.core.domain.model.Task
import com.example.releaseflow.core.domain.usecase.project.GetProjectByIdUseCase
import com.example.releaseflow.core.domain.usecase.task.GetTasksByProjectUseCase
import com.example.releaseflow.core.domain.usecase.task.ToggleTaskCompletionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTasksByProjectUseCase: GetTasksByProjectUseCase,
    private val toggleTaskCompletionUseCase: ToggleTaskCompletionUseCase
) : ViewModel() {

    private val _project = MutableStateFlow<Project?>(null)
    val project: StateFlow<Project?> = _project.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun loadProject(projectId: Long) {
        viewModelScope.launch {
            getProjectByIdUseCase(projectId).collect { _project.value = it }
        }
        viewModelScope.launch {
            getTasksByProjectUseCase(projectId).collect { _tasks.value = it }
        }
    }

    fun toggleTaskCompletion(taskId: Long) {
        viewModelScope.launch {
            toggleTaskCompletionUseCase(taskId)
        }
    }
}
