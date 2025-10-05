package com.example.releaseflow.core.domain.usecase.project

import com.example.releaseflow.core.domain.model.Project
import com.example.releaseflow.core.domain.repository.ProjectRepository
import com.example.releaseflow.core.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Use case for deleting a project
 * Also deletes all associated tasks (handled by foreign key cascade)
 */
class DeleteProjectUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    /**
     * Delete a project and all its tasks
     * 
     * @param project Project to delete
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(project: Project): Result<Unit> {
        return try {
            // Delete tasks first (if cascade doesn't handle it)
            taskRepository.deleteTasksByProject(project.id)
            
            // Delete project
            projectRepository.deleteProject(project)
            Result.success(Unit)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
