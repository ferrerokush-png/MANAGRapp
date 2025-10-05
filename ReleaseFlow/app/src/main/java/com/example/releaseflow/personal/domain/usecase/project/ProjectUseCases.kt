package com.example.releaseflow.personal.domain.usecase.project

import com.example.releaseflow.core.domain.model.Project
import com.example.releaseflow.core.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.first

class ListProjectsUseCase(private val repo: ProjectRepository) {
    suspend operator fun invoke(): List<Project> = repo.getAllProjects().first()
}

class GetProjectUseCase(private val repo: ProjectRepository) {
    suspend operator fun invoke(projectId: Long): Project? = repo.getProjectById(projectId).first()
}

class UpsertProjectUseCase(private val repo: ProjectRepository) {
    suspend operator fun invoke(project: Project) = repo.updateProject(project)
}

class DeleteProjectUseCase(private val repo: ProjectRepository) {
    suspend operator fun invoke(projectId: Long) = repo.deleteProject(Project(id = projectId, title = "", artistName = "", type = com.example.releaseflow.core.domain.model.ReleaseType.SINGLE, releaseDate = java.time.LocalDate.now(), artworkUri = null, genre = "", status = com.example.releaseflow.core.domain.model.ProjectStatus.PLANNING, createdAt = 0, updatedAt = 0))
}

