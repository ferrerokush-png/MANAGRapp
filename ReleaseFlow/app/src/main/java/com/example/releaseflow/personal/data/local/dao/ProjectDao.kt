package com.example.releaseflow.personal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.releaseflow.personal.data.local.entity.ReleaseProject
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ReleaseProject): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<ReleaseProject>): List<Long>

    @Update
    suspend fun updateProject(project: ReleaseProject): Int

    @Delete
    suspend fun deleteProject(project: ReleaseProject): Int

    @Query("SELECT * FROM release_projects ORDER BY release_date ASC")
    fun getAllProjects(): Flow<List<ReleaseProject>>

    @Query("SELECT * FROM release_projects WHERE id = :id LIMIT 1")
    fun getProjectById(id: Long): Flow<ReleaseProject?>

    @Query("SELECT * FROM release_projects WHERE status = :status ORDER BY release_date ASC")
    fun getProjectsByStatus(status: String): Flow<List<ReleaseProject>>

    @Query("DELETE FROM release_projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long): Int

    @Query("UPDATE release_projects SET status = :status WHERE id = :id")
    suspend fun updateProjectStatus(id: Long, status: String)

    @Query("UPDATE release_projects SET completion_percentage = :percentage, completed_tasks = :completedTasks, total_tasks = :totalTasks WHERE id = :id")
    suspend fun updateProjectCompletion(id: Long, percentage: Float, completedTasks: Int, totalTasks: Int)

    @Query("SELECT COUNT(*) FROM release_projects WHERE status = :status")
    suspend fun getProjectCountByStatus(status: String): Int

    @Query("SELECT COUNT(*) FROM release_projects")
    suspend fun getTotalProjectCount(): Int
}
