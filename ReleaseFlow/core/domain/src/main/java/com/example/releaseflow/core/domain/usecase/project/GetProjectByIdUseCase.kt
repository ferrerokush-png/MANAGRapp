package com.example.releaseflow.core.domain.usecase.project

import com.example.releaseflow.core.domain.model.Project
import com.example.releaseflow.core.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting a project by ID
 */
class GetProjectByIdUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    /**
     * Get project by ID
     * 
     * @param projectId Project ID
     * @return Flow of project or null if not found
     */
    operator fun invoke(projectId: Long): Flow<Project?> {
        return projectRepository.getProjectById(projectId)
    }
}
