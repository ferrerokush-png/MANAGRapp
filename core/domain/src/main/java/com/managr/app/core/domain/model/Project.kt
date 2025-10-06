package com.managr.app.core.domain.model

import java.time.LocalDate

/**
 * Domain model for a music release project
 * Represents a single, EP, or album release with all associated metadata
 */
data class Project(
    val id: Long = 0,
    val title: String,
    val artistName: String = "",
    val type: ReleaseType,
    val releaseDate: LocalDate,
    val artworkUri: String? = null,
    val genre: String? = null,
    val subGenres: List<String> = emptyList(),
    val trackCount: Int = 1,
    val description: String = "",
    val isrc: String? = null, // International Standard Recording Code
    val upc: String? = null, // Universal Product Code
    val recordLabel: String? = null,
    val distributorType: DistributorType = DistributorType.DISTROKID,
    val uploadDeadline: LocalDate? = null, // Auto-calculated based on distributor
    val status: ProjectStatus = ProjectStatus.PLANNING,
    val completionPercentage: Float = 0f,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val tags: List<String> = emptyList(),
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if project is overdue
     */
    fun isOverdue(): Boolean {
        val today = LocalDate.now()
        return releaseDate.isBefore(today) && status != ProjectStatus.RELEASED
    }

    /**
     * Check if project is upcoming (within next 30 days)
     */
    fun isUpcoming(): Boolean {
        val today = LocalDate.now()
        val thirtyDaysFromNow = today.plusDays(30)
        return releaseDate.isAfter(today) && releaseDate.isBefore(thirtyDaysFromNow)
    }

    /**
     * Calculate days until release
     */
    fun daysUntilRelease(): Long {
        val today = LocalDate.now()
        return java.time.temporal.ChronoUnit.DAYS.between(today, releaseDate)
    }

    /**
     * Calculate days until upload deadline
     */
    fun daysUntilUploadDeadline(): Long? {
        uploadDeadline ?: return null
        val today = LocalDate.now()
        return java.time.temporal.ChronoUnit.DAYS.between(today, uploadDeadline)
    }

    /**
     * Validate project data
     */
    fun validate(): Result<Unit> {
        return when {
            title.isBlank() -> Result.failure(IllegalArgumentException("Title cannot be empty"))
            trackCount < 1 -> Result.failure(IllegalArgumentException("Track count must be at least 1"))
            trackCount !in type.trackCountRange() -> Result.failure(
                IllegalArgumentException("Track count ${trackCount} is not typical for ${type.displayName()}")
            )
            releaseDate.isBefore(LocalDate.now().minusYears(1)) -> Result.failure(
                IllegalArgumentException("Release date cannot be more than 1 year in the past")
            )
            else -> Result.success(Unit)
        }
    }
}

/**
 * Project status in the release workflow
 */
enum class ProjectStatus(val displayName: String) {
    PLANNING("Planning"),
    IN_PROGRESS("In Progress"),
    READY_FOR_UPLOAD("Ready for Upload"),
    UPLOADED("Uploaded"),
    RELEASED("Released"),
    ARCHIVED("Archived");

    fun isActive(): Boolean = this in listOf(PLANNING, IN_PROGRESS, READY_FOR_UPLOAD, UPLOADED)
}
