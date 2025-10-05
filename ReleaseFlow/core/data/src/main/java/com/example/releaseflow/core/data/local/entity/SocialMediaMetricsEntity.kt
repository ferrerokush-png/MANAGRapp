package com.example.releaseflow.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.releaseflow.core.domain.model.SocialMediaMetrics
import com.example.releaseflow.core.domain.model.SocialPlatform
import java.time.LocalDate

/**
 * Room entity for Social Media Metrics
 */
@Entity(
    tableName = "social_media_metrics",
    indices = [Index("projectId"), Index("platform"), Index("date")]
)
data class SocialMediaMetricsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long?,
    val platform: String, // SocialPlatform as String
    val date: LocalDate,
    val followers: Long,
    val posts: Int,
    val views: Long,
    val likes: Long,
    val comments: Long,
    val shares: Long,
    val reach: Long,
    val impressions: Long,
    val profileVisits: Long,
    val linkClicks: Long,
    val videoViews: Long,
    val averageWatchTime: Int,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * Map SocialMediaMetricsEntity to domain SocialMediaMetrics
 */
fun SocialMediaMetricsEntity.toDomain(): SocialMediaMetrics {
    return SocialMediaMetrics(
        id = id,
        projectId = projectId,
        platform = SocialPlatform.valueOf(platform),
        date = date,
        followers = followers,
        posts = posts,
        views = views,
        likes = likes,
        comments = comments,
        shares = shares,
        reach = reach,
        impressions = impressions,
        profileVisits = profileVisits,
        linkClicks = linkClicks,
        videoViews = videoViews,
        averageWatchTime = averageWatchTime,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

/**
 * Map domain SocialMediaMetrics to SocialMediaMetricsEntity
 */
fun SocialMediaMetrics.toEntity(): SocialMediaMetricsEntity {
    return SocialMediaMetricsEntity(
        id = id,
        projectId = projectId,
        platform = platform.name,
        date = date,
        followers = followers,
        posts = posts,
        views = views,
        likes = likes,
        comments = comments,
        shares = shares,
        reach = reach,
        impressions = impressions,
        profileVisits = profileVisits,
        linkClicks = linkClicks,
        videoViews = videoViews,
        averageWatchTime = averageWatchTime,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
