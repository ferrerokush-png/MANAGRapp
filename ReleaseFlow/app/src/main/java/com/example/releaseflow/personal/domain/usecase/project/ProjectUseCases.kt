package com.example.releaseflow.personal.domain.usecase.project

import com.example.releaseflow.personal.domain.model.Project
import com.example.releaseflow.personal.domain.repository.ProjectRepository

class ListProjectsUseCase(private val repo: ProjectRepository) {
    suspend operator fun invoke(): List<Project> = repo.listProjects()
}

class GetProjectUseCase(private val repo: ProjectRepository) {
    suspend operator fun invoke(projectId: String): Project? = repo.getProject(projectId)
}

class UpsertProjectUseCase(private val repo: ProjectRepository) {
    suspend operator fun invoke(project: Project) = repo.upsertProject(project)
}

class DeleteProjectUseCase(private val repo: ProjectRepository) {
    suspend operator fun invoke(projectId: String) = repo.deleteProject(projectId)
}

