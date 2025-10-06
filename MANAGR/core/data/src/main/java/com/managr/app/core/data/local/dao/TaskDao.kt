package com.managr.app.core.data.local.dao

import androidx.room.*
import com.managr.app.core.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * DAO for Task operations
 */
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY `order` ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY `order` ASC")
    fun getTasksByProject(projectId: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Long): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId AND status = :status ORDER BY `order` ASC")
    fun getTasksByStatus(projectId: Long, status: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId AND phase = :phase ORDER BY `order` ASC")
    fun getTasksByPhase(projectId: Long, phase: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDate < :today AND status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY dueDate ASC")
    fun getAllOverdueTasks(today: LocalDate): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId AND dueDate < :today AND status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY dueDate ASC")
    fun getOverdueTasksByProject(projectId: Long, today: LocalDate): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDate BETWEEN :today AND :futureDate AND status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY dueDate ASC")
    fun getAllTasksDueWithinDays(today: LocalDate, futureDate: LocalDate): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId AND dueDate BETWEEN :today AND :futureDate AND status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY dueDate ASC")
    fun getTasksDueWithinDaysByProject(projectId: Long, today: LocalDate, futureDate: LocalDate): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDate BETWEEN :startDate AND :endDate ORDER BY dueDate ASC")
    fun getTasksByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE projectId = :projectId AND status = 'COMPLETED'")
    suspend fun getCompletedTasksCount(projectId: Long): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE projectId = :projectId")
    suspend fun getTotalTasksCount(projectId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>): List<Long>

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE tasks SET status = :status, updatedAt = :updatedAt WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Long, status: String, updatedAt: Long)

    @Query("UPDATE tasks SET status = CASE WHEN status = 'COMPLETED' THEN 'PENDING' ELSE 'COMPLETED' END, updatedAt = :updatedAt WHERE id = :taskId")
    suspend fun toggleTaskCompletion(taskId: Long, updatedAt: Long)

    @Update
    suspend fun updateTasks(tasks: List<TaskEntity>)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE projectId = :projectId")
    suspend fun deleteTasksByProject(projectId: Long)
}
