@file:Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate")

package com.example.releaseflow.personal.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.releaseflow.personal.data.local.dao.ProjectDao
import com.example.releaseflow.personal.data.local.dao.TaskDao
import com.example.releaseflow.personal.data.local.entity.ReleaseProject
import com.example.releaseflow.personal.data.local.entity.ReleaseTask
import com.example.releaseflow.personal.data.local.entity.TaskCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val projectId: Long = savedStateHandle.get<Long>("projectId") ?: 0L

    // Fetch the specific project by ID
    val project: StateFlow<ReleaseProject?> =
        projectDao.getProjectById(projectId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    // Observe tasks for this project
    val tasks: StateFlow<List<ReleaseTask>> =
        taskDao.getTasksForProject(projectId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addTask(title: String, description: String, deadlineMillis: Long, category: TaskCategory = TaskCategory.PRODUCTION) {
        if (title.isBlank()) return
        viewModelScope.launch {
            taskDao.insertTask(
                ReleaseTask(
                    projectId = projectId,
                    title = title.trim(),
                    description = description.trim(),
                    dueDate = Date(deadlineMillis),
                    category = category,
                    isCompleted = false
                )
            )
        }
    }

    fun updateTask(task: ReleaseTask, newTitle: String, newDescription: String, newDeadlineMillis: Long) {
        if (newTitle.isBlank()) return
        viewModelScope.launch {
            taskDao.updateTask(task.copy(title = newTitle.trim(), description = newDescription.trim(), dueDate = Date(newDeadlineMillis)))
        }
    }

    fun deleteTask(task: ReleaseTask) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }

    fun toggleTaskCompletion(task: ReleaseTask, completed: Boolean) {
        viewModelScope.launch {
            taskDao.updateTask(task.copy(isCompleted = completed))
        }
    }

    fun updateArtwork(artworkUri: String) {
        viewModelScope.launch {
            val current = project.value ?: return@launch
            projectDao.updateProject(current.copy(artworkPath = artworkUri))
        }
    }
}
