package com.example.releaseflow.core.domain.model

import java.time.LocalDate

/**
 * Distributor configuration for a project
 */
data class Distributor(
    val id: Long = 0,
    val projectId: Long,
    val type: DistributorType,
    val accountEmail: String? = null,
    val uploadDeadline: LocalDate,
    val uploadedDate: LocalDate? = null,
    val uploadStatus: UploadStatus = UploadStatus.NOT_STARTED,
    val releaseUrl: String? = null,
    val upc: String? = null,
    val isrc: String? = null,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if upload is overdue
     */
    fun isUploadOverdue(): Boolean {
        return LocalDate.now().isAfter(uploadDeadline) && 
               uploadStatus != UploadStatus.UPLOADED &&
               uploadStatus != UploadStatus.LIVE
    }

    /**
     * Calculate days until upload deadline
     */
    fun daysUntilDeadline(): Long {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), uploadDeadline)
    }

    /**
     * Check if deadline is approaching (within 7 days)
     */
    fun isDeadlineApproaching(): Boolean {
        val daysLeft = daysUntilDeadline()
        return daysLeft in 1..7 && uploadStatus != UploadStatus.UPLOADED
    }

    /**
     * Calculate upload deadline from release date
     */
    companion object {
        fun calculateUploadDeadline(releaseDate: LocalDate, distributorType: DistributorType): LocalDate {
            return releaseDate.minusDays(distributorType.minUploadDays.toLong())
        }
    }
}

/**
 * Upload status for distributor
 */
enum class UploadStatus(val displayName: String) {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    UPLOADED("Uploaded"),
    PROCESSING("Processing"),
    LIVE("Live"),
    FAILED("Failed");

    fun isComplete(): Boolean = this in listOf(UPLOADED, LIVE)
    fun needsAction(): Boolean = this in listOf(NOT_STARTED, FAILED)
}

/**
 * Promotional asset (artwork, videos, press releases, etc.)
 */
data class PromotionalAsset(
    val id: Long = 0,
    val projectId: Long,
    val name: String,
    val type: AssetType,
    val fileUri: String,
    val fileSize: Long = 0, // in bytes
    val mimeType: String? = null,
    val description: String = "",
    val tags: List<String> = emptyList(),
    val isPublic: Boolean = false,
    val downloadUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Get human-readable file size
     */
    fun formattedFileSize(): String {
        return when {
            fileSize < 1024 -> "$fileSize B"
            fileSize < 1024 * 1024 -> "${fileSize / 1024} KB"
            fileSize < 1024 * 1024 * 1024 -> "${fileSize / (1024 * 1024)} MB"
            else -> "${fileSize / (1024 * 1024 * 1024)} GB"
        }
    }

    /**
     * Check if asset is an image
     */
    fun isImage(): Boolean = type in listOf(AssetType.ARTWORK, AssetType.SOCIAL_MEDIA_POST, AssetType.PHOTO)

    /**
     * Check if asset is a video
     */
    fun isVideo(): Boolean = type in listOf(AssetType.MUSIC_VIDEO, AssetType.LYRIC_VIDEO, AssetType.PROMO_VIDEO)

    /**
     * Validate asset data
     */
    fun validate(): Result<Unit> {
        return when {
            name.isBlank() -> Result.failure(IllegalArgumentException("Name cannot be empty"))
            fileUri.isBlank() -> Result.failure(IllegalArgumentException("File URI cannot be empty"))
            fileSize <= 0 -> Result.failure(IllegalArgumentException("Invalid file size"))
            else -> Result.success(Unit)
        }
    }
}

/**
 * Type of promotional asset
 */
enum class AssetType(val displayName: String) {
    ARTWORK("Artwork"),
    MUSIC_VIDEO("Music Video"),
    LYRIC_VIDEO("Lyric Video"),
    PROMO_VIDEO("Promo Video"),
    SOCIAL_MEDIA_POST("Social Media Post"),
    PRESS_RELEASE("Press Release"),
    EPK("Electronic Press Kit"),
    PHOTO("Photo"),
    AUDIO_SNIPPET("Audio Snippet"),
    OTHER("Other");

    fun isVisual(): Boolean = this in listOf(ARTWORK, MUSIC_VIDEO, LYRIC_VIDEO, PROMO_VIDEO, SOCIAL_MEDIA_POST, PHOTO)
}
