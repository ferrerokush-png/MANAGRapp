package com.example.releaseflow.core.domain.model

import java.time.LocalDate

/**
 * CRM contact for industry professionals (curators, bloggers, etc.)
 */
data class CRMContact(
    val id: Long = 0,
    val name: String,
    val type: ContactType,
    val email: String? = null,
    val phone: String? = null,
    val company: String? = null,
    val platform: SocialPlatform? = null,
    val socialHandle: String? = null,
    val website: String? = null,
    val genres: List<String> = emptyList(), // Genres they focus on
    val playlistName: String? = null, // For playlist curators
    val playlistFollowers: Long? = null,
    val playlistUrl: String? = null,
    val relationshipStrength: RelationshipStrength = RelationshipStrength.NEW,
    val lastContactDate: LocalDate? = null,
    val nextFollowUpDate: LocalDate? = null,
    val notes: String = "",
    val tags: List<String> = emptyList(),
    val submissionHistory: List<Long> = emptyList(), // Project IDs
    val responseRate: Float = 0f, // Percentage (0-100)
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if follow-up is due
     */
    fun isFollowUpDue(): Boolean {
        nextFollowUpDate ?: return false
        return !LocalDate.now().isBefore(nextFollowUpDate)
    }

    /**
     * Check if contact has been inactive (no contact in 90 days)
     */
    fun isInactive(): Boolean {
        lastContactDate ?: return true
        val ninetyDaysAgo = LocalDate.now().minusDays(90)
        return lastContactDate.isBefore(ninetyDaysAgo)
    }

    /**
     * Calculate days since last contact
     */
    fun daysSinceLastContact(): Long? {
        lastContactDate ?: return null
        return java.time.temporal.ChronoUnit.DAYS.between(lastContactDate, LocalDate.now())
    }

    /**
     * Check if contact is relevant for a genre
     */
    fun isRelevantForGenre(genre: String): Boolean {
        return genres.isEmpty() || genres.any { it.equals(genre, ignoreCase = true) }
    }

    /**
     * Get priority score for outreach (0-100)
     */
    fun outreachPriority(): Float {
        var score = 50f
        
        // Relationship strength
        score += when (relationshipStrength) {
            RelationshipStrength.STRONG -> 20f
            RelationshipStrength.GOOD -> 10f
            RelationshipStrength.NEUTRAL -> 0f
            RelationshipStrength.NEW -> -10f
            RelationshipStrength.COLD -> -20f
        }
        
        // Response rate
        score += (responseRate / 10f)
        
        // Playlist size (for curators)
        if (type == ContactType.PLAYLIST_CURATOR && playlistFollowers != null) {
            score += when {
                playlistFollowers >= 100000 -> 20f
                playlistFollowers >= 10000 -> 10f
                playlistFollowers >= 1000 -> 5f
                else -> 0f
            }
        }
        
        // Recent contact
        if (isInactive()) score -= 15f
        
        return score.coerceIn(0f, 100f)
    }

    /**
     * Validate contact data
     */
    fun validate(): Result<Unit> {
        return when {
            name.isBlank() -> Result.failure(IllegalArgumentException("Name cannot be empty"))
            email.isNullOrBlank() && socialHandle.isNullOrBlank() -> Result.failure(
                IllegalArgumentException("Must provide either email or social handle")
            )
            type == ContactType.PLAYLIST_CURATOR && playlistName.isNullOrBlank() -> Result.failure(
                IllegalArgumentException("Playlist name required for playlist curators")
            )
            else -> Result.success(Unit)
        }
    }
}

/**
 * Relationship strength with contact
 */
enum class RelationshipStrength(val displayName: String, val score: Int) {
    COLD("Cold", 1),
    NEW("New", 2),
    NEUTRAL("Neutral", 3),
    GOOD("Good", 4),
    STRONG("Strong", 5);

    fun isPositive(): Boolean = this in listOf(GOOD, STRONG)
}

/**
 * Submission to a contact
 */
data class Submission(
    val id: Long = 0,
    val projectId: Long,
    val contactId: Long,
    val submissionDate: LocalDate,
    val status: SubmissionStatus = SubmissionStatus.PENDING,
    val response: String = "",
    val responseDate: LocalDate? = null,
    val followUpDate: LocalDate? = null,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if submission is pending response
     */
    fun isPendingResponse(): Boolean {
        return status == SubmissionStatus.PENDING && responseDate == null
    }

    /**
     * Calculate days since submission
     */
    fun daysSinceSubmission(): Long {
        return java.time.temporal.ChronoUnit.DAYS.between(submissionDate, LocalDate.now())
    }

    /**
     * Check if follow-up is needed (14 days with no response)
     */
    fun needsFollowUp(): Boolean {
        return isPendingResponse() && daysSinceSubmission() >= 14
    }
}

/**
 * Submission status
 */
enum class SubmissionStatus(val displayName: String) {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    NO_RESPONSE("No Response"),
    FOLLOW_UP_SENT("Follow-up Sent");

    fun isFinal(): Boolean = this in listOf(ACCEPTED, REJECTED, NO_RESPONSE)
}
