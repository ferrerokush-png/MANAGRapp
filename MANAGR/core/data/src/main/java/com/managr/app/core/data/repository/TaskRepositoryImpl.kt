package com.managr.app.core.data.repository

import com.managr.app.core.data.local.dao.TaskDao
import com.managr.app.core.data.local.entity.toEntity
import com.managr.app.core.data.local.entity.toDomain
import com.managr.app.core.domain.model.Task
import com.managr.app.core.domain.model.TaskPhase
import com.managr.app.core.domain.model.TaskStatus
import com.managr.app.core.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasks().map { it.map { entity -> entity.toDomain() } }

    override fun getTasksByProject(projectId: Long): Flow<List<Task>> =
        taskDao.getTasksByProject(projectId).map { it.map { entity -> entity.toDomain() } }

    override fun getTaskById(id: Long): Flow<Task?> =
        taskDao.getTaskById(id).map { it?.toDomain() }

    override fun getTasksByStatus(projectId: Long, status: TaskStatus): Flow<List<Task>> =
        taskDao.getTasksByStatus(projectId, status.name).map { it.map { entity -> entity.toDomain() } }

    override fun getTasksByPhase(projectId: Long, phase: TaskPhase): Flow<List<Task>> =
        taskDao.getTasksByPhase(projectId, phase.name).map { it.map { entity -> entity.toDomain() } }

    override fun getOverdueTasks(projectId: Long?): Flow<List<Task>> {
        val today = LocalDate.now()
        return if (projectId != null) {
            taskDao.getOverdueTasksByProject(projectId, today).map { it.map { entity -> entity.toDomain() } }
        } else {
            taskDao.getAllOverdueTasks(today).map { it.map { entity -> entity.toDomain() } }
        }
    }

    override fun getTasksDueWithinDays(days: Int, projectId: Long?): Flow<List<Task>> {
        val today = LocalDate.now()
        val futureDate = today.plusDays(days.toLong())
        return if (projectId != null) {
            taskDao.getTasksDueWithinDaysByProject(projectId, today, futureDate).map { it.map { entity -> entity.toDomain() } }
        } else {
            taskDao.getAllTasksDueWithinDays(today, futureDate).map { it.map { entity -> entity.toDomain() } }
        }
    }

    override fun getTasksByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Task>> =
        taskDao.getTasksByDateRange(startDate, endDate).map { it.map { entity -> entity.toDomain() } }

    override suspend fun getCompletedTasksCount(projectId: Long): Int =
        taskDao.getCompletedTasksCount(projectId)

    override suspend fun getTotalTasksCount(projectId: Long): Int =
        taskDao.getTotalTasksCount(projectId)

    override suspend fun insertTask(task: Task): Long =
        taskDao.insertTask(task.toEntity())

    override suspend fun insertTasks(tasks: List<Task>): List<Long> =
        taskDao.insertTasks(tasks.map { it.toEntity() })

    override suspend fun updateTask(task: Task) =
        taskDao.updateTask(task.toEntity())

    override suspend fun updateTaskStatus(taskId: Long, status: TaskStatus) =
        taskDao.updateTaskStatus(taskId, status.name, System.currentTimeMillis())

    override suspend fun toggleTaskCompletion(taskId: Long) =
        taskDao.toggleTaskCompletion(taskId, System.currentTimeMillis())

    override suspend fun updateTaskOrder(tasks: List<Task>) =
        taskDao.updateTasks(tasks.map { it.toEntity() })

    override suspend fun deleteTask(task: Task) =
        taskDao.deleteTask(task.toEntity())

    override suspend fun deleteTasksByProject(projectId: Long) =
        taskDao.deleteTasksByProject(projectId)
}
