package com.example.releaseflow.personal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.releaseflow.personal.data.local.entity.ReleaseProject
import com.example.releaseflow.personal.data.local.entity.ReleaseTask
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Insert operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ReleaseProject): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: ReleaseTask): Long

    // Query operations
    @Query("SELECT * FROM release_projects ORDER BY release_date ASC")
    fun getAllProjects(): Flow<List<ReleaseProject>>

    @Query("SELECT * FROM release_projects WHERE id = :id LIMIT 1")
    fun getProjectById(id: Long): Flow<ReleaseProject?>

    @Query("SELECT * FROM release_tasks WHERE project_id = :projectId ORDER BY deadline ASC")
    fun getTasksForProject(projectId: Long): Flow<List<ReleaseTask>>

    // Update operations
    @Update
    suspend fun updateProject(project: ReleaseProject): Int

    @Update
    suspend fun updateTask(task: ReleaseTask): Int

    // Delete operations
    @Delete
    suspend fun deleteTask(task: ReleaseTask): Int
}
