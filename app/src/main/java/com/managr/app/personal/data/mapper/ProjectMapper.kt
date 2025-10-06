package com.managr.app.personal.data.mapper

import com.managr.app.core.domain.model.Project
import com.managr.app.core.domain.model.ProjectStatus as CoreProjectStatus
import com.managr.app.core.domain.model.ReleaseType as CoreReleaseType
import com.managr.app.personal.data.local.entity.ReleaseProject
import com.managr.app.personal.data.local.entity.ProjectStatus as PersonalProjectStatus
import com.managr.app.personal.data.local.entity.ReleaseType as PersonalReleaseType
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

/**
 * Mapper to convert between core domain Project and personal module ReleaseProject
 */
object ProjectMapper {
    
    fun toCoreProject(releaseProject: ReleaseProject): Project {
        return Project(
            id = releaseProject.id,
            title = releaseProject.title,
            artistName = releaseProject.artistName,
            type = releaseProject.releaseType.toCoreReleaseType(),
            releaseDate = releaseProject.releaseDate.toLocalDate(),
            artworkUri = releaseProject.artworkPath,
            genre = releaseProject.genre,
            status = releaseProject.status.toCoreProjectStatus(),
            createdAt = releaseProject.createdAt.time,
            updatedAt = releaseProject.updatedAt.time,
            completionPercentage = releaseProject.completionPercentage,
            completedTasks = releaseProject.completedTasks,
            totalTasks = releaseProject.totalTasks
        )
    }
    
    fun toReleaseProject(project: Project): ReleaseProject {
        return ReleaseProject(
            id = project.id,
            title = project.title,
            artistName = project.artistName,
            releaseType = project.type.toPersonalReleaseType(),
            releaseDate = project.releaseDate.toDate(),
            artworkPath = project.artworkUri,
            genre = project.genre,
            status = project.status.toPersonalProjectStatus(),
            createdAt = Date(project.createdAt),
            updatedAt = Date(project.updatedAt),
            completionPercentage = project.completionPercentage,
            completedTasks = project.completedTasks,
            totalTasks = project.totalTasks
        )
    }
    
    private fun PersonalReleaseType.toCoreReleaseType(): CoreReleaseType {
        return when (this) {
            PersonalReleaseType.SINGLE -> CoreReleaseType.SINGLE
            PersonalReleaseType.EP -> CoreReleaseType.EP
            PersonalReleaseType.ALBUM -> CoreReleaseType.ALBUM
        }
    }
    
    private fun CoreReleaseType.toPersonalReleaseType(): PersonalReleaseType {
        return when (this) {
            CoreReleaseType.SINGLE -> PersonalReleaseType.SINGLE
            CoreReleaseType.EP -> PersonalReleaseType.EP
            CoreReleaseType.ALBUM -> PersonalReleaseType.ALBUM
        }
    }
    
    private fun PersonalProjectStatus.toCoreProjectStatus(): CoreProjectStatus {
        return when (this) {
            PersonalProjectStatus.ACTIVE -> CoreProjectStatus.IN_PROGRESS
            PersonalProjectStatus.COMPLETED -> CoreProjectStatus.RELEASED
            PersonalProjectStatus.DRAFT -> CoreProjectStatus.PLANNING
        }
    }
    
    private fun CoreProjectStatus.toPersonalProjectStatus(): PersonalProjectStatus {
        return when (this) {
            CoreProjectStatus.PLANNING -> PersonalProjectStatus.DRAFT
            CoreProjectStatus.IN_PROGRESS -> PersonalProjectStatus.ACTIVE
            CoreProjectStatus.READY_FOR_UPLOAD -> PersonalProjectStatus.ACTIVE
            CoreProjectStatus.UPLOADED -> PersonalProjectStatus.ACTIVE
            CoreProjectStatus.RELEASED -> PersonalProjectStatus.COMPLETED
            CoreProjectStatus.ARCHIVED -> PersonalProjectStatus.COMPLETED
        }
    }
    
    private fun Date.toLocalDate(): LocalDate {
        return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
    
    private fun LocalDate.toDate(): Date {
        return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}
