package com.example.releaseflow.personal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.releaseflow.personal.data.local.entity.ReleaseTask
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: ReleaseTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<ReleaseTask>): List<Long>

    @Update
    suspend fun updateTask(task: ReleaseTask): Int

    @Delete
    suspend fun deleteTask(task: ReleaseTask): Int

    @Query("SELECT * FROM release_tasks WHERE project_id = :projectId ORDER BY due_date ASC")
    fun getTasksForProject(projectId: Long): Flow<List<ReleaseTask>>

    @Query("SELECT * FROM release_tasks WHERE id = :id LIMIT 1")
    fun getTaskById(id: Long): Flow<ReleaseTask?>

    @Query("SELECT * FROM release_tasks WHERE is_completed = :isCompleted ORDER BY due_date ASC")
    fun getTasksByCompletion(isCompleted: Boolean): Flow<List<ReleaseTask>>

    @Query("SELECT * FROM release_tasks ORDER BY due_date ASC")
    fun getAllTasks(): Flow<List<ReleaseTask>>

    @Query("DELETE FROM release_tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long): Int
}
