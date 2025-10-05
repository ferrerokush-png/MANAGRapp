package com.example.releaseflow.personal.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.releaseflow.personal.data.local.dao.TaskDao
import com.example.releaseflow.personal.data.local.entity.ReleaseTask
import com.example.releaseflow.personal.data.local.entity.TaskCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TaskListUiState {
    object Loading : TaskListUiState()
    data class Success(val tasks: List<ReleaseTask>) : TaskListUiState()
    data class Error(val message: String) : TaskListUiState()
}

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    private val _currentProjectId = MutableStateFlow<Long?>(null)
    
    private val _selectedCategory = MutableStateFlow<TaskCategory?>(null)
    val selectedCategory: StateFlow<TaskCategory?> = _selectedCategory.asStateFlow()

    private val _showCompleted = MutableStateFlow(true)
    val showCompleted: StateFlow<Boolean> = _showCompleted.asStateFlow()

    private val _uiState = MutableStateFlow<TaskListUiState>(TaskListUiState.Loading)
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    fun loadTasks(projectId: Long) {
        _currentProjectId.value = projectId
        viewModelScope.launch {
            try {
                combine(
                    taskDao.getTasksForProject(projectId),
                    _selectedCategory,
                    _showCompleted
                ) { tasks, category, showCompleted ->
                    tasks
                        .filter { task ->
                            (category == null || task.category == category) &&
                            (showCompleted || !task.isCompleted)
                        }
                        .sortedBy { it.dueDate }
                }.catch { e ->
                    _uiState.value = TaskListUiState.Error(e.message ?: "Failed to load tasks")
                }.collect { filteredTasks ->
                    _uiState.value = TaskListUiState.Success(filteredTasks)
                }
            } catch (e: Exception) {
                _uiState.value = TaskListUiState.Error(e.message ?: "Failed to load tasks")
            }
        }
    }

    fun filterByCategory(category: TaskCategory?) {
        _selectedCategory.value = category
    }

    fun toggleShowCompleted(show: Boolean) {
        _showCompleted.value = show
    }

    fun toggleTaskComplete(task: ReleaseTask) {
        viewModelScope.launch {
            try {
                taskDao.updateTask(task.copy(isCompleted = !task.isCompleted))
            } catch (e: Exception) {
                _uiState.value = TaskListUiState.Error(e.message ?: "Failed to update task")
            }
        }
    }

    fun deleteTask(task: ReleaseTask) {
        viewModelScope.launch {
            try {
                taskDao.deleteTask(task)
            } catch (e: Exception) {
                _uiState.value = TaskListUiState.Error(e.message ?: "Failed to delete task")
            }
        }
    }
}

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    fun addTask(
        projectId: Long,
        title: String,
        description: String?,
        dueDate: java.util.Date,
        priority: Int,
        category: TaskCategory,
        assignedTo: String?
    ) {
        viewModelScope.launch {
            try {
                taskDao.insertTask(
                    ReleaseTask(
                        projectId = projectId,
                        title = title,
                        description = description,
                        dueDate = dueDate,
                        priority = priority,
                        category = category,
                        assignedTo = assignedTo,
                        isCompleted = false
                    )
                )
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
