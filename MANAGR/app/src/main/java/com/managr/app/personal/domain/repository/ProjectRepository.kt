package com.managr.app.personal.domain.repository

import com.managr.app.personal.data.local.entity.ReleaseProject
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getAllProjects(): Flow<List<ReleaseProject>>
    fun getProjectById(id: Long): Flow<ReleaseProject?>
    fun getProjectsByStatus(status: String): Flow<List<ReleaseProject>>
    suspend fun insertProject(project: ReleaseProject): Long
    suspend fun updateProject(project: ReleaseProject): Int
    suspend fun deleteProject(project: ReleaseProject): Int
    suspend fun deleteProjectById(id: Long): Int
}
