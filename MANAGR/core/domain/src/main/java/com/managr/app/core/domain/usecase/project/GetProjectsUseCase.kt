package com.managr.app.core.domain.usecase.project

import com.managr.app.core.domain.model.Project
import com.managr.app.core.domain.model.ProjectStatus
import com.managr.app.core.domain.model.ReleaseType
import com.managr.app.core.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for getting projects with various filters
 */
class GetProjectsUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    /**
     * Get all projects
     */
    fun getAllProjects(): Flow<List<Project>> {
        return projectRepository.getAllProjects()
    }
    
    /**
     * Get active projects (not archived or released)
     */
    fun getActiveProjects(): Flow<List<Project>> {
        return projectRepository.getActiveProjects()
    }
    
    /**
     * Get upcoming releases (within next 30 days)
     */
    fun getUpcomingReleases(): Flow<List<Project>> {
        return projectRepository.getUpcomingReleases()
    }
    
    /**
     * Get projects by status
     */
    fun getProjectsByStatus(status: ProjectStatus): Flow<List<Project>> {
        return projectRepository.getProjectsByStatus(status)
    }
    
    /**
     * Get projects by type
     */
    fun getProjectsByType(type: ReleaseType): Flow<List<Project>> {
        return projectRepository.getProjectsByType(type)
    }
    
    /**
     * Search projects
     */
    fun searchProjects(query: String): Flow<List<Project>> {
        return projectRepository.searchProjects(query)
    }
    
    /**
     * Get projects by date range
     */
    fun getProjectsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Project>> {
        return projectRepository.getProjectsByDateRange(startDate, endDate)
    }
}
