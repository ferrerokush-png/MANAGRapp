package com.managr.app.core.domain.usecase.project

import com.managr.app.core.domain.model.*
import com.managr.app.core.domain.util.Constants
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for generating project templates with tasks
 * Creates industry-standard task lists based on release type
 */
class GenerateProjectTemplateUseCase @Inject constructor() {
    /**
     * Generate a complete project template with tasks
     * 
     * @param title Project title
     * @param artistName Artist name
     * @param type Release type (Single/EP/Album)
     * @param releaseDate Release date
     * @param genre Optional genre
     * @param distributorType Distributor type
     * @return List of tasks for the project
     */
    operator fun invoke(
        title: String,
        artistName: String,
        type: ReleaseType,
        releaseDate: LocalDate,
        genre: String? = null,
        distributorType: DistributorType = DistributorType.DISTROKID
    ): List<Task> {
        val tasks = mutableListOf<Task>()
        val uploadDeadline = releaseDate.minusDays(distributorType.minUploadDays.toLong())
        
        // Common tasks for all release types
        tasks.addAll(generateCommonTasks(releaseDate, uploadDeadline, distributorType))
        
        // Type-specific tasks
        when (type) {
            ReleaseType.SINGLE -> tasks.addAll(generateSingleTasks(releaseDate))
            ReleaseType.EP -> tasks.addAll(generateEPTasks(releaseDate))
            ReleaseType.ALBUM -> tasks.addAll(generateAlbumTasks(releaseDate))
        }
        
        // Sort by due date and assign order
        return tasks
            .sortedBy { it.dueDate }
            .mapIndexed { index, task -> task.copy(order = index) }
    }
    
    private fun generateCommonTasks(releaseDate: LocalDate, uploadDeadline: LocalDate, distributor: DistributorType): List<Task> {
        return listOf(
            Task(
                projectId = 0, // Will be set when inserted
                title = "Finalize Master",
                description = "Complete final mixing and mastering",
                dueDate = releaseDate.minusDays(28),
                phase = TaskPhase.PRODUCTION,
                priority = TaskPriority.CRITICAL
            ),
            Task(
                projectId = 0,
                title = "Create Artwork (1500x1500px)",
                description = "Design cover art meeting distributor requirements",
                dueDate = releaseDate.minusDays(24),
                phase = TaskPhase.PRODUCTION,
                priority = TaskPriority.HIGH
            ),
            Task(
                projectId = 0,
                title = "Upload to ${distributor.displayName}",
                description = "Upload tracks, artwork, and metadata. Required for editorial playlist pitching",
                dueDate = uploadDeadline,
                phase = TaskPhase.DISTRIBUTION,
                priority = TaskPriority.CRITICAL
            ),
            Task(
                projectId = 0,
                title = "Pitch to Editorial Playlists",
                description = "Submit to Spotify for Artists editorial consideration",
                dueDate = uploadDeadline,
                phase = TaskPhase.PROMOTION,
                priority = TaskPriority.HIGH
            ),
            Task(
                projectId = 0,
                title = "Schedule Social Media Teasers",
                description = "Plan and schedule announcement posts",
                dueDate = releaseDate.minusDays(20),
                phase = TaskPhase.PROMOTION,
                priority = TaskPriority.MEDIUM
            ),
            Task(
                projectId = 0,
                title = "Send Press Release / EPK",
                description = "Send electronic press kit to media contacts",
                dueDate = releaseDate.minusDays(18),
                phase = TaskPhase.PROMOTION,
                priority = TaskPriority.MEDIUM
            ),
            Task(
                projectId = 0,
                title = "Announce Pre-Save Campaign",
                description = "Launch pre-save links on social media",
                dueDate = releaseDate.minusDays(18),
                phase = TaskPhase.PROMOTION,
                priority = TaskPriority.HIGH
            ),
            Task(
                projectId = 0,
                title = "Create TikTok/Reels Content (3 clips)",
                description = "Shoot short-form video content for promotion",
                dueDate = releaseDate.minusDays(16),
                phase = TaskPhase.PROMOTION,
                priority = TaskPriority.HIGH
            ),
            Task(
                projectId = 0,
                title = "Send Email Newsletter",
                description = "Announce release to email list",
                dueDate = releaseDate.minusDays(14),
                phase = TaskPhase.PROMOTION,
                priority = TaskPriority.MEDIUM
            ),
            Task(
                projectId = 0,
                title = "Upload YouTube Visualizer",
                description = "Upload audio with visualizer to YouTube",
                dueDate = releaseDate.minusDays(7),
                phase = TaskPhase.PROMOTION,
                priority = TaskPriority.MEDIUM
            ),
            Task(
                projectId = 0,
                title = "Release Day Post (All Platforms)",
                description = "Post release announcement on all social media",
                dueDate = releaseDate,
                phase = TaskPhase.PROMOTION,
                priority = TaskPriority.CRITICAL
            ),
            Task(
                projectId = 0,
                title = "Thank You / Follow-up Post",
                description = "Thank fans for support",
                dueDate = releaseDate.plusDays(1),
                phase = TaskPhase.POST_RELEASE,
                priority = TaskPriority.LOW
            ),
            Task(
                projectId = 0,
                title = "Pitch to Independent Curators",
                description = "Submit to independent playlist curators",
                dueDate = releaseDate.plusDays(2),
                phase = TaskPhase.POST_RELEASE,
                priority = TaskPriority.MEDIUM
            )
        )
    }
    
    private fun generateSingleTasks(releaseDate: LocalDate): List<Task> {
        return listOf(
            Task(
                projectId = 0,
                title = "Lyric Video (Optional)",
                description = "Create and upload lyric video",
                dueDate = releaseDate.plusDays(3),
                phase = TaskPhase.POST_RELEASE,
                priority = TaskPriority.LOW
            )
        )
    }
    
    private fun generateEPTasks(releaseDate: LocalDate): List<Task> {
        return listOf(
            Task(
                projectId = 0,
                title = "Finalize EP Tracklist",
                description = "Confirm track order and titles",
                dueDate = releaseDate.minusDays(30),
                phase = TaskPhase.PRE_PRODUCTION,
                priority = TaskPriority.HIGH
            ),
            Task(
                projectId = 0,
                title = "Create Artwork Variations",
                description = "Design artwork for each track",
                dueDate = releaseDate.minusDays(25),
                phase = TaskPhase.PRODUCTION,
                priority = TaskPriority.MEDIUM
            ),
            Task(
                projectId = 0,
                title = "Create 12 Cinematic Mini-Videos",
                description = "Produce short promotional videos",
                dueDate = releaseDate.minusDays(15),
                phase = TaskPhase.PROMOTION,
                priority = TaskPriority.MEDIUM
            )
        )
    }
    
    private fun generateAlbumTasks(releaseDate: LocalDate): List<Task> {
        return listOf(
            Task(
                projectId = 0,
                title = "Finalize Album Sequence + ISRCs",
                description = "Confirm track order and register ISRCs",
                dueDate = releaseDate.minusDays(35),
                phase = TaskPhase.PRE_PRODUCTION,
                priority = TaskPriority.CRITICAL
            ),
            Task(
                projectId = 0,
                title = "Create Album Trailer Video",
                description = "Produce album announcement trailer",
                dueDate = releaseDate.minusDays(20),
                phase = TaskPhase.PROMOTION,
                priority = TaskPriority.HIGH
            ),
            Task(
                projectId = 0,
                title = "Press Outreach Wave 2",
                description = "Follow-up press campaign",
                dueDate = releaseDate.plusDays(7),
                phase = TaskPhase.POST_RELEASE,
                priority = TaskPriority.MEDIUM
            )
        )
    }
}
