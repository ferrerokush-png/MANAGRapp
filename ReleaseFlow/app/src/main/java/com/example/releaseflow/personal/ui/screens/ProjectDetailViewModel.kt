@file:Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate")

package com.example.releaseflow.personal.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.releaseflow.personal.data.local.dao.AppDao
import com.example.releaseflow.personal.data.local.entity.ReleaseProject
import com.example.releaseflow.personal.data.local.entity.ReleaseTask
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val dao: AppDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val projectId: Long = savedStateHandle.get<Long>("projectId") ?: 0L

    // Fetch the specific project by ID
    val project: StateFlow<ReleaseProject?> =
        dao.getProjectById(projectId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    // Observe tasks for this project
    val tasks: StateFlow<List<ReleaseTask>> =
        dao.getTasksForProject(projectId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addTask(description: String, deadlineMillis: Long) {
        if (description.isBlank()) return
        viewModelScope.launch {
            dao.insertTask(
                ReleaseTask(
                    projectId = projectId,
                    description = description.trim(),
                    deadline = deadlineMillis,
                    isCompleted = false
                )
            )
        }
    }

    fun updateTask(task: ReleaseTask, newDescription: String, newDeadlineMillis: Long) {
        if (newDescription.isBlank()) return
        viewModelScope.launch {
            dao.updateTask(task.copy(description = newDescription.trim(), deadline = newDeadlineMillis))
        }
    }

    fun deleteTask(task: ReleaseTask) {
        viewModelScope.launch {
            dao.deleteTask(task)
        }
    }

    fun toggleTaskCompletion(task: ReleaseTask, completed: Boolean) {
        viewModelScope.launch {
            dao.updateTask(task.copy(isCompleted = completed))
        }
    }

    fun updateArtwork(artworkUri: String) {
        viewModelScope.launch {
            val current = project.value ?: return@launch
            dao.updateProject(current.copy(artworkUri = artworkUri))
        }
    }
}
