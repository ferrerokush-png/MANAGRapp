package com.example.releaseflow.personal.data.repository

import com.example.releaseflow.core.domain.model.Project
import com.example.releaseflow.core.domain.model.ProjectStatus
import com.example.releaseflow.core.domain.model.ReleaseType
import com.example.releaseflow.core.domain.repository.ProjectRepository
import com.example.releaseflow.personal.data.local.dao.ProjectDao
import com.example.releaseflow.personal.data.local.entity.ReleaseProject
import com.example.releaseflow.personal.data.mapper.ProjectMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao
) : ProjectRepository {
    
    override fun getAllProjects(): Flow<List<Project>> = 
        projectDao.getAllProjects().map { projects -> projects.map { ProjectMapper.toCoreProject(it) } }

    override fun getProjectById(id: Long): Flow<Project?> = 
        projectDao.getProjectById(id).map { it?.let { ProjectMapper.toCoreProject(it) } }

    override fun getActiveProjects(): Flow<List<Project>> = 
        projectDao.getProjectsByStatus("ACTIVE").map { projects -> projects.map { ProjectMapper.toCoreProject(it) } }

    override fun getUpcomingReleases(): Flow<List<Project>> = 
        projectDao.getAllProjects().map { projects -> 
            projects.map { ProjectMapper.toCoreProject(it) }
                .filter { it.isUpcoming() }
        }

    override fun getProjectsByStatus(status: ProjectStatus): Flow<List<Project>> = 
        projectDao.getProjectsByStatus(status.name).map { projects -> projects.map { ProjectMapper.toCoreProject(it) } }

    override fun getProjectsByType(type: ReleaseType): Flow<List<Project>> = 
        projectDao.getAllProjects().map { projects -> 
            projects.map { ProjectMapper.toCoreProject(it) }
                .filter { it.type == type }
        }

    override fun searchProjects(query: String): Flow<List<Project>> = 
        projectDao.getAllProjects().map { projects -> 
            projects.map { ProjectMapper.toCoreProject(it) }
                .filter { it.title.contains(query, ignoreCase = true) || it.artistName.contains(query, ignoreCase = true) }
        }

    override fun getProjectsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Project>> = 
        projectDao.getAllProjects().map { projects -> 
            projects.map { ProjectMapper.toCoreProject(it) }
                .filter { it.releaseDate.isAfter(startDate) && it.releaseDate.isBefore(endDate) }
        }

    override suspend fun insertProject(project: Project): Long = 
        projectDao.insertProject(ProjectMapper.toReleaseProject(project))

    override suspend fun updateProject(project: Project) {
        projectDao.updateProject(ProjectMapper.toReleaseProject(project))
    }

    override suspend fun updateProjectStatus(projectId: Long, status: ProjectStatus) {
        projectDao.updateProjectStatus(projectId, status.name)
    }

    override suspend fun updateProjectCompletion(projectId: Long, percentage: Float, completedTasks: Int, totalTasks: Int) {
        projectDao.updateProjectCompletion(projectId, percentage, completedTasks, totalTasks)
    }

    override suspend fun deleteProject(project: Project) {
        projectDao.deleteProject(ProjectMapper.toReleaseProject(project))
    }

    override suspend fun archiveProject(projectId: Long) {
        projectDao.updateProjectStatus(projectId, ProjectStatus.ARCHIVED.name)
    }

    override suspend fun getProjectCountByStatus(status: ProjectStatus): Int = 
        projectDao.getProjectCountByStatus(status.name)

    override suspend fun getTotalProjectCount(): Int = 
        projectDao.getTotalProjectCount()
}
