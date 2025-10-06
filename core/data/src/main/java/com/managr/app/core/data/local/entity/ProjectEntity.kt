package com.managr.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.managr.app.core.domain.model.*
import java.time.LocalDate

/**
 * Room entity for Project
 */
@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val artistName: String,
    val type: String, // ReleaseType as String
    val releaseDate: LocalDate,
    val artworkUri: String?,
    val genre: String?,
    val subGenres: List<String>,
    val trackCount: Int,
    val description: String,
    val isrc: String?,
    val upc: String?,
    val recordLabel: String?,
    val distributorType: String, // DistributorType as String
    val uploadDeadline: LocalDate?,
    val status: String, // ProjectStatus as String
    val completionPercentage: Float,
    val totalTasks: Int,
    val completedTasks: Int,
    val tags: List<String>,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * Map ProjectEntity to domain Project
 */
fun ProjectEntity.toDomain(): Project {
    return Project(
        id = id,
        title = title,
        artistName = artistName,
        type = ReleaseType.valueOf(type),
        releaseDate = releaseDate,
        artworkUri = artworkUri,
        genre = genre,
        subGenres = subGenres,
        trackCount = trackCount,
        description = description,
        isrc = isrc,
        upc = upc,
        recordLabel = recordLabel,
        distributorType = DistributorType.valueOf(distributorType),
        uploadDeadline = uploadDeadline,
        status = ProjectStatus.valueOf(status),
        completionPercentage = completionPercentage,
        totalTasks = totalTasks,
        completedTasks = completedTasks,
        tags = tags,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

/**
 * Map domain Project to ProjectEntity
 */
fun Project.toEntity(): ProjectEntity {
    return ProjectEntity(
        id = id,
        title = title,
        artistName = artistName,
        type = type.name,
        releaseDate = releaseDate,
        artworkUri = artworkUri,
        genre = genre,
        subGenres = subGenres,
        trackCount = trackCount,
        description = description,
        isrc = isrc,
        upc = upc,
        recordLabel = recordLabel,
        distributorType = distributorType.name,
        uploadDeadline = uploadDeadline,
        status = status.name,
        completionPercentage = completionPercentage,
        totalTasks = totalTasks,
        completedTasks = completedTasks,
        tags = tags,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
