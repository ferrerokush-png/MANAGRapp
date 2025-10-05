package com.example.releaseflow.core.data.local.dao

import androidx.room.*
import com.example.releaseflow.core.data.local.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * DAO for Project operations
 */
@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY releaseDate ASC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: Long): Flow<ProjectEntity?>

    @Query("SELECT * FROM projects WHERE status = :status ORDER BY releaseDate ASC")
    fun getProjectsByStatus(status: String): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE type = :type ORDER BY releaseDate ASC")
    fun getProjectsByType(type: String): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE status NOT IN ('RELEASED', 'ARCHIVED') ORDER BY releaseDate ASC")
    fun getActiveProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE releaseDate BETWEEN :today AND :futureDate ORDER BY releaseDate ASC")
    fun getUpcomingReleases(today: LocalDate, futureDate: LocalDate): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE releaseDate BETWEEN :startDate AND :endDate ORDER BY releaseDate ASC")
    fun getProjectsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE title LIKE '%' || :query || '%' OR artistName LIKE '%' || :query || '%' ORDER BY releaseDate ASC")
    fun searchProjects(query: String): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("UPDATE projects SET status = :status, updatedAt = :updatedAt WHERE id = :projectId")
    suspend fun updateProjectStatus(projectId: Long, status: String, updatedAt: Long)

    @Query("UPDATE projects SET completionPercentage = :percentage, completedTasks = :completedTasks, totalTasks = :totalTasks, updatedAt = :updatedAt WHERE id = :projectId")
    suspend fun updateProjectCompletion(projectId: Long, percentage: Float, completedTasks: Int, totalTasks: Int, updatedAt: Long)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("UPDATE projects SET status = 'ARCHIVED', updatedAt = :updatedAt WHERE id = :projectId")
    suspend fun archiveProject(projectId: Long, updatedAt: Long)

    @Query("SELECT COUNT(*) FROM projects WHERE status = :status")
    suspend fun getProjectCountByStatus(status: String): Int

    @Query("SELECT COUNT(*) FROM projects")
    suspend fun getTotalProjectCount(): Int
}
