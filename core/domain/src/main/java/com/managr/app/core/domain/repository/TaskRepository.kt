package com.managr.app.core.domain.repository

import com.managr.app.core.domain.model.Task
import com.managr.app.core.domain.model.TaskPhase
import com.managr.app.core.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for task operations
 */
interface TaskRepository {
    /**
     * Get all tasks
     */
    fun getAllTasks(): Flow<List<Task>>
    
    /**
     * Get all tasks for a project
     */
    fun getTasksByProject(projectId: Long): Flow<List<Task>>

    /**
     * Get task by ID
     */
    fun getTaskById(id: Long): Flow<Task?>

    /**
     * Get tasks by status
     */
    fun getTasksByStatus(projectId: Long, status: TaskStatus): Flow<List<Task>>

    /**
     * Get tasks by phase
     */
    fun getTasksByPhase(projectId: Long, phase: TaskPhase): Flow<List<Task>>

    /**
     * Get overdue tasks
     */
    fun getOverdueTasks(projectId: Long? = null): Flow<List<Task>>

    /**
     * Get tasks due within days
     */
    fun getTasksDueWithinDays(days: Int, projectId: Long? = null): Flow<List<Task>>

    /**
     * Get tasks for date range
     */
    fun getTasksByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Task>>

    /**
     * Get completed tasks count for project
     */
    suspend fun getCompletedTasksCount(projectId: Long): Int

    /**
     * Get total tasks count for project
     */
    suspend fun getTotalTasksCount(projectId: Long): Int

    /**
     * Insert new task
     */
    suspend fun insertTask(task: Task): Long

    /**
     * Insert multiple tasks
     */
    suspend fun insertTasks(tasks: List<Task>): List<Long>

    /**
     * Update existing task
     */
    suspend fun updateTask(task: Task)

    /**
     * Update task status
     */
    suspend fun updateTaskStatus(taskId: Long, status: TaskStatus)

    /**
     * Toggle task completion
     */
    suspend fun toggleTaskCompletion(taskId: Long)

    /**
     * Update task order for drag-and-drop
     */
    suspend fun updateTaskOrder(tasks: List<Task>)

    /**
     * Delete task
     */
    suspend fun deleteTask(task: Task)

    /**
     * Delete all tasks for a project
     */
    suspend fun deleteTasksByProject(projectId: Long)
}
