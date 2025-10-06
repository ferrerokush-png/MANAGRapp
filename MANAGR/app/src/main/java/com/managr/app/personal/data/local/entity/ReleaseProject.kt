package com.managr.app.personal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

enum class ReleaseType {
    SINGLE, EP, ALBUM
}

enum class ProjectStatus {
    ACTIVE, COMPLETED, DRAFT
}

@Entity(tableName = "release_projects")
data class ReleaseProject(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "artist_name")
    val artistName: String,

    @ColumnInfo(name = "release_type")
    val releaseType: ReleaseType,

    @ColumnInfo(name = "release_date")
    val releaseDate: Date,

    @ColumnInfo(name = "artwork_path")
    val artworkPath: String? = null,

    @ColumnInfo(name = "genre")
    val genre: String? = null,

    @ColumnInfo(name = "status")
    val status: ProjectStatus = ProjectStatus.ACTIVE,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),

    @ColumnInfo(name = "completion_percentage")
    val completionPercentage: Float = 0f,

    @ColumnInfo(name = "completed_tasks")
    val completedTasks: Int = 0,

    @ColumnInfo(name = "total_tasks")
    val totalTasks: Int = 0
)
