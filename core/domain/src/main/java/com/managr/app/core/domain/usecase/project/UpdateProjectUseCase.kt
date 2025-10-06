package com.managr.app.core.domain.usecase.project

import com.managr.app.core.domain.model.Project
import com.managr.app.core.domain.repository.ProjectRepository
import com.managr.app.core.domain.util.ValidationRules

/**
 * Use case for updating a project
 */
class UpdateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    /**
     * Update an existing project
     * 
     * @param project Project to update
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(project: Project): Result<Unit> {
        return try {
            // Validate project
            project.validate().getOrElse { return Result.failure(it) }
            
            // Additional validation
            if (!ValidationRules.isValidProjectTitle(project.title)) {
                return Result.failure(IllegalArgumentException("Invalid project title"))
            }
            
            if (!ValidationRules.isValidReleaseDate(project.releaseDate)) {
                return Result.failure(IllegalArgumentException("Invalid release date"))
            }
            
            // Update timestamp
            val updatedProject = project.copy(updatedAt = System.currentTimeMillis())
            
            // Update project
            projectRepository.updateProject(updatedProject)
            Result.success(Unit)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
