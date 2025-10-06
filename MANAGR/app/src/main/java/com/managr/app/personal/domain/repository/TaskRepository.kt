package com.managr.app.personal.domain.repository

import com.managr.app.personal.data.local.entity.ReleaseTask
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<ReleaseTask>>
    fun getTaskById(id: Long): Flow<ReleaseTask?>
    fun getTasksForProject(projectId: Long): Flow<List<ReleaseTask>>
    fun getTasksByCompletion(isCompleted: Boolean): Flow<List<ReleaseTask>>
    suspend fun insertTask(task: ReleaseTask): Long
    suspend fun updateTask(task: ReleaseTask): Int
    suspend fun deleteTask(task: ReleaseTask): Int
    suspend fun deleteTaskById(id: Long): Int
}
