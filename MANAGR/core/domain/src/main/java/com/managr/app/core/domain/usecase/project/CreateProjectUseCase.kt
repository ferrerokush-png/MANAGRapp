package com.managr.app.core.domain.usecase.project

import com.managr.app.core.domain.model.Project
import com.managr.app.core.domain.model.ProjectStatus
import com.managr.app.core.domain.repository.ProjectRepository
import com.managr.app.core.domain.util.ValidationRules
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for creating a new project
 * Validates project data and calculates upload deadline
 */
class CreateProjectUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    /**
     * Create a new project
     * 
     * @param project Project to create
     * @return Result with project ID or error
     */
    suspend operator fun invoke(project: Project): Result<Long> {
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
            
            if (!ValidationRules.isValidTrackCount(project.trackCount, project.type)) {
                return Result.failure(IllegalArgumentException("Track count doesn't match release type"))
            }
            
            // Calculate upload deadline if not set
            val projectWithDeadline = if (project.uploadDeadline == null) {
                val deadline = project.releaseDate.minusDays(project.distributorType.minUploadDays.toLong())
                project.copy(uploadDeadline = deadline)
            } else {
                project
            }
            
            // Set initial status if not set
            val projectToSave = if (projectWithDeadline.status == ProjectStatus.PLANNING) {
                projectWithDeadline
            } else {
                projectWithDeadline.copy(status = ProjectStatus.PLANNING)
            }
            
            // Insert project
            val projectId = projectRepository.insertProject(projectToSave)
            Result.success(projectId)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
