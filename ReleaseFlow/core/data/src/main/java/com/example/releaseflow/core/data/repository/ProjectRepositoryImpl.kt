package com.example.releaseflow.core.data.repository

import com.example.releaseflow.core.data.local.dao.ProjectDao
import com.example.releaseflow.core.data.local.entity.toEntity
import com.example.releaseflow.core.data.local.entity.toDomain
import com.example.releaseflow.core.domain.model.Project
import com.example.releaseflow.core.domain.model.ProjectStatus
import com.example.releaseflow.core.domain.model.ReleaseType
import com.example.releaseflow.core.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao
) : ProjectRepository {

    override fun getAllProjects(): Flow<List<Project>> =
        projectDao.getAllProjects().map { it.map { entity -> entity.toDomain() } }

    override fun getProjectById(id: Long): Flow<Project?> =
        projectDao.getProjectById(id).map { it?.toDomain() }

    override fun getProjectsByStatus(status: ProjectStatus): Flow<List<Project>> =
        projectDao.getProjectsByStatus(status.name).map { it.map { entity -> entity.toDomain() } }

    override fun getProjectsByType(type: ReleaseType): Flow<List<Project>> =
        projectDao.getProjectsByType(type.name).map { it.map { entity -> entity.toDomain() } }

    override fun getActiveProjects(): Flow<List<Project>> =
        projectDao.getActiveProjects().map { it.map { entity -> entity.toDomain() } }

    override fun getUpcomingReleases(): Flow<List<Project>> {
        val today = LocalDate.now()
        val futureDate = today.plusDays(30)
        return projectDao.getUpcomingReleases(today, futureDate).map { it.map { entity -> entity.toDomain() } }
    }

    override fun getProjectsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Project>> =
        projectDao.getProjectsByDateRange(startDate, endDate).map { it.map { entity -> entity.toDomain() } }

    override fun searchProjects(query: String): Flow<List<Project>> =
        projectDao.searchProjects(query).map { it.map { entity -> entity.toDomain() } }

    override suspend fun insertProject(project: Project): Long =
        projectDao.insertProject(project.toEntity())

    override suspend fun updateProject(project: Project) =
        projectDao.updateProject(project.toEntity())

    override suspend fun updateProjectStatus(projectId: Long, status: ProjectStatus) =
        projectDao.updateProjectStatus(projectId, status.name, System.currentTimeMillis())

    override suspend fun updateProjectCompletion(projectId: Long, percentage: Float, completedTasks: Int, totalTasks: Int) =
        projectDao.updateProjectCompletion(projectId, percentage, completedTasks, totalTasks, System.currentTimeMillis())

    override suspend fun deleteProject(project: Project) =
        projectDao.deleteProject(project.toEntity())

    override suspend fun archiveProject(projectId: Long) =
        projectDao.archiveProject(projectId, System.currentTimeMillis())

    override suspend fun getProjectCountByStatus(status: ProjectStatus): Int =
        projectDao.getProjectCountByStatus(status.name)

    override suspend fun getTotalProjectCount(): Int =
        projectDao.getTotalProjectCount()
}
