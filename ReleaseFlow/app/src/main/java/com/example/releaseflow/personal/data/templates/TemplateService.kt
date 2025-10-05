package com.example.releaseflow.personal.data.templates

import com.example.releaseflow.personal.data.local.entity.ReleaseTask
import com.example.releaseflow.personal.data.local.entity.TaskCategory
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateService @Inject constructor() {

    private val singleTasks = listOf(
        TaskTemplate("Finalize Master", -21, 1, TaskCategory.PRODUCTION, "Complete final mastering and approve audio quality"),
        TaskTemplate("Create Artwork 1500x1500px", -18, 2, TaskCategory.PRODUCTION, "Design and finalize cover artwork meeting distributor specs"),
        TaskTemplate("Upload to Distributor", -21, 1, TaskCategory.DISTRIBUTION, "Submit master and metadata to distribution platform"),
        TaskTemplate("Pitch to Editorial Playlists", -21, 1, TaskCategory.MARKETING, "Submit to Spotify and Apple Music editorial teams"),
        TaskTemplate("Social Media Content Creation", -14, 2, TaskCategory.MARKETING, "Create teasers, behind-the-scenes content, and promotional posts"),
        TaskTemplate("Press Release Draft", -10, 3, TaskCategory.MARKETING, "Write and distribute press release to media outlets"),
        TaskTemplate("Submit to Music Blogs", -14, 2, TaskCategory.MARKETING, "Pitch to relevant music blogs and online publications"),
        TaskTemplate("Schedule Social Posts", -7, 2, TaskCategory.MARKETING, "Queue up release day and post-release social media content")
    )

    private val epAdditionalTasks = listOf(
        TaskTemplate("Create EPK (Electronic Press Kit)", -28, 2, TaskCategory.MARKETING, "Compile bio, photos, streaming links, and press quotes"),
        TaskTemplate("Plan Album Release Party/Livestream", -14, 3, TaskCategory.MARKETING, "Organize virtual or in-person release event"),
        TaskTemplate("Coordinate Music Video Production", -30, 2, TaskCategory.PRODUCTION, "Plan and execute music video for lead single")
    )

    private val albumAdditionalTasks = listOf(
        TaskTemplate("Register Songs with PRO", -45, 1, TaskCategory.DISTRIBUTION, "Register all tracks with performance rights organization"),
        TaskTemplate("Secure Pre-Save/Pre-Order Campaigns", -30, 1, TaskCategory.MARKETING, "Set up pre-save links across all platforms"),
        TaskTemplate("Plan Multi-Single Release Strategy", -60, 1, TaskCategory.MARKETING, "Schedule and coordinate release of multiple singles before album"),
        TaskTemplate("Book Press Interviews", -21, 2, TaskCategory.MARKETING, "Schedule podcast, radio, and blog interviews"),
        TaskTemplate("Organize Album Tour/Shows", -45, 2, TaskCategory.MARKETING, "Book venues and promote live performance dates")
    )

    /**
     * Generate tasks based on project template and release date
     */
    fun generateTasksForTemplate(
        projectId: Long,
        template: ProjectTemplate,
        releaseDate: Date
    ): List<ReleaseTask> {
        val taskTemplates = when (template) {
            ProjectTemplate.SINGLE -> singleTasks
            ProjectTemplate.EP -> singleTasks + epAdditionalTasks
            ProjectTemplate.ALBUM -> singleTasks + epAdditionalTasks + albumAdditionalTasks
        }

        return taskTemplates.map { taskTemplate ->
            val dueDate = calculateDueDate(releaseDate, taskTemplate.dueDaysOffset)
            ReleaseTask(
                projectId = projectId,
                title = taskTemplate.title,
                description = taskTemplate.description,
                dueDate = dueDate,
                isCompleted = false,
                priority = taskTemplate.priority,
                category = taskTemplate.category,
                createdAt = Date()
            )
        }
    }

    /**
     * Calculate due date based on release date and offset
     */
    private fun calculateDueDate(releaseDate: Date, daysOffset: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = releaseDate
        calendar.add(Calendar.DAY_OF_YEAR, daysOffset)
        return calendar.time
    }

    /**
     * Get task count for template
     */
    fun getTaskCountForTemplate(template: ProjectTemplate): Int {
        return when (template) {
            ProjectTemplate.SINGLE -> singleTasks.size
            ProjectTemplate.EP -> singleTasks.size + epAdditionalTasks.size
            ProjectTemplate.ALBUM -> singleTasks.size + epAdditionalTasks.size + albumAdditionalTasks.size
        }
    }

    /**
     * Get all task templates for a project type
     */
    fun getTemplatesForType(template: ProjectTemplate): List<TaskTemplate> {
        return when (template) {
            ProjectTemplate.SINGLE -> singleTasks
            ProjectTemplate.EP -> singleTasks + epAdditionalTasks
            ProjectTemplate.ALBUM -> singleTasks + epAdditionalTasks + albumAdditionalTasks
        }
    }
}
