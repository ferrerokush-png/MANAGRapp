package com.example.releaseflow.personal.domain.repository

import com.example.releaseflow.personal.domain.model.Project

interface ProjectRepository {
    suspend fun listProjects(): List<Project>
    suspend fun getProject(projectId: String): Project?
    suspend fun upsertProject(project: Project)
    suspend fun deleteProject(projectId: String)
}

