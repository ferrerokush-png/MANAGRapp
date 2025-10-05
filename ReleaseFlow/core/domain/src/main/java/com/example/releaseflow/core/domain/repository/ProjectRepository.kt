package com.example.releaseflow.core.domain.repository

import com.example.releaseflow.core.domain.model.Project
import com.example.releaseflow.core.domain.model.ProjectStatus
import com.example.releaseflow.core.domain.model.ReleaseType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for project operations
 */
interface ProjectRepository {
    /**
     * Get all projects
     */
    fun getAllProjects(): Flow<List<Project>>

    /**
     * Get project by ID
     */
    fun getProjectById(id: Long): Flow<Project?>

    /**
     * Get projects by status
     */
    fun getProjectsByStatus(status: ProjectStatus): Flow<List<Project>>

    /**
     * Get projects by release type
     */
    fun getProjectsByType(type: ReleaseType): Flow<List<Project>>

    /**
     * Get active projects (not archived or released)
     */
    fun getActiveProjects(): Flow<List<Project>>

    /**
     * Get upcoming releases (within next 30 days)
     */
    fun getUpcomingReleases(): Flow<List<Project>>

    /**
     * Get projects by date range
     */
    fun getProjectsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Project>>

    /**
     * Search projects by title or artist
     */
    fun searchProjects(query: String): Flow<List<Project>>

    /**
     * Insert new project
     */
    suspend fun insertProject(project: Project): Long

    /**
     * Update existing project
     */
    suspend fun updateProject(project: Project)

    /**
     * Update project status
     */
    suspend fun updateProjectStatus(projectId: Long, status: ProjectStatus)

    /**
     * Update project completion percentage
     */
    suspend fun updateProjectCompletion(projectId: Long, percentage: Float, completedTasks: Int, totalTasks: Int)

    /**
     * Delete project
     */
    suspend fun deleteProject(project: Project)

    /**
     * Archive project
     */
    suspend fun archiveProject(projectId: Long)

    /**
     * Get project count by status
     */
    suspend fun getProjectCountByStatus(status: ProjectStatus): Int

    /**
     * Get total project count
     */
    suspend fun getTotalProjectCount(): Int
}
