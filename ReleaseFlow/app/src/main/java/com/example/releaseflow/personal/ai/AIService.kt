package com.example.releaseflow.personal.ai

import com.example.releaseflow.personal.data.local.dao.AIQueryDao
import com.example.releaseflow.personal.data.local.entity.AIQuery
import com.example.releaseflow.personal.data.local.entity.AIQueryType
import com.example.releaseflow.personal.data.local.entity.ReleaseProject
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIService @Inject constructor(
    private val geminiService: GeminiService,
    private val aiQueryDao: AIQueryDao
) {
    companion object {
        private const val MAX_QUERIES_PER_MONTH = 10
    }

    /**
     * Check if user has remaining queries for the current month
     */
    suspend fun hasRemainingQueries(): Pair<Boolean, Int> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        val startOfMonth = calendar.timeInMillis
        val used = aiQueryDao.getQueryCountSince(startOfMonth)
        val remaining = MAX_QUERIES_PER_MONTH - used
        
        return Pair(remaining > 0, remaining)
    }

    /**
     * Generate analytics insights for a project
     */
    suspend fun generateAnalyticsInsights(
        projectTitle: String,
        streamData: String,
        platformBreakdown: String
    ): Result<String> {
        val (hasQueries, remaining) = hasRemainingQueries()
        if (!hasQueries) {
            return Result.failure(RateLimitException("Monthly query limit reached. $remaining queries remaining."))
        }

        val prompt = """
            Analyze this streaming data for "$projectTitle" and provide 3 actionable insights for improving performance.
            
            Stream Data: $streamData
            Platform Breakdown: $platformBreakdown
            
            Provide specific, actionable recommendations focused on:
            1. Which platforms to focus marketing efforts on
            2. Content strategies to boost engagement
            3. Timing and promotional tactics
            
            Keep insights concise and practical.
        """.trimIndent()

        return executeQuery(AIQueryType.ANALYTICS_INSIGHTS, prompt)
    }

    /**
     * Generate task suggestions based on project type and timeline
     */
    suspend fun generateTaskSuggestions(
        releaseType: String,
        weeksUntilRelease: Int,
        genre: String?
    ): Result<String> {
        val (hasQueries, remaining) = hasRemainingQueries()
        if (!hasQueries) {
            return Result.failure(RateLimitException("Monthly query limit reached. $remaining queries remaining."))
        }

        val genreContext = genre?.let { " in the $it genre" } ?: ""
        val prompt = """
            Based on this $releaseType$genreContext releasing in $weeksUntilRelease weeks, suggest 5 additional marketing tasks.
            
            Consider:
            - Current timeline and urgency
            - Platform-specific marketing opportunities
            - Industry best practices for $releaseType releases
            - Social media and content marketing strategies
            
            Format as a numbered list with brief explanations.
        """.trimIndent()

        return executeQuery(AIQueryType.TASK_SUGGESTIONS, prompt)
    }

    /**
     * Generate playlist pitch
     */
    suspend fun generatePlaylistPitch(
        trackTitle: String,
        artistName: String,
        genre: String,
        targetAudience: String,
        trackDescription: String?
    ): Result<String> {
        val (hasQueries, remaining) = hasRemainingQueries()
        if (!hasQueries) {
            return Result.failure(RateLimitException("Monthly query limit reached. $remaining queries remaining."))
        }

        val descContext = trackDescription?.let { "\n\nTrack Description: $it" } ?: ""
        val prompt = """
            Create a professional playlist pitch for this $genre track targeting $targetAudience.
            
            Track: "$trackTitle" by $artistName$descContext
            
            The pitch should:
            1. Be 2-3 paragraphs
            2. Highlight the track's unique qualities and appeal
            3. Explain why it fits the playlist's audience
            4. Include specific hooks that make it playlist-worthy
            5. Maintain a professional yet enthusiastic tone
            
            Write it ready to copy and paste into a playlist submission.
        """.trimIndent()

        return executeQuery(AIQueryType.PLAYLIST_PITCH, prompt)
    }

    /**
     * Generate release strategy recommendations
     */
    suspend fun generateReleaseStrategy(
        project: ReleaseProject
    ): Result<String> {
        val (hasQueries, remaining) = hasRemainingQueries()
        if (!hasQueries) {
            return Result.failure(RateLimitException("Monthly query limit reached. $remaining queries remaining."))
        }

        val genreContext = project.genre?.let { " in the $it genre" } ?: ""
        val prompt = """
            Provide a comprehensive release strategy for:
            
            Project: "${project.title}" by ${project.artistName}
            Type: ${project.releaseType.name}$genreContext
            Release Date: ${project.releaseDate}
            
            Include recommendations for:
            1. Pre-release timeline and key milestones
            2. Platform-specific marketing strategies
            3. Social media content ideas
            4. Playlist and blog pitching approach
            5. Post-release momentum tactics
            
            Focus on actionable, realistic strategies for independent artists.
        """.trimIndent()

        return executeQuery(AIQueryType.RELEASE_STRATEGY, prompt, project.id)
    }

    /**
     * Execute query and save to history
     */
    private suspend fun executeQuery(
        queryType: AIQueryType,
        prompt: String,
        projectId: Long? = null
    ): Result<String> {
        return geminiService.generate(prompt).fold(
            onSuccess = { response ->
                // Save to history
                aiQueryDao.insertQuery(
                    AIQuery(
                        queryType = queryType,
                        prompt = prompt,
                        response = response,
                        projectId = projectId,
                        createdAt = Date()
                    )
                )
                Result.success(response)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    /**
     * Mark query as helpful/not helpful
     */
    suspend fun markQueryHelpful(queryId: Long, helpful: Boolean) {
        val query = aiQueryDao.getRecentQueries(1000).let { flow ->
            // Simple approach: get recent queries and find by ID
            // In production, add getQueryById method
        }
        // TODO: Implement update when needed
    }
}

class RateLimitException(message: String) : Exception(message)
