package com.example.releaseflow.core.domain.model

import java.time.LocalDateTime

data class SocialPost(
    val id: String = "",
    val projectId: Long,
    val platform: SocialPlatform,
    val content: String,
    val mediaUrls: List<String> = emptyList(),
    val scheduledAt: LocalDateTime? = null,
    val postedAt: LocalDateTime? = null,
    val status: PostStatus = PostStatus.DRAFT,
    val hashtags: List<String> = emptyList(),
    val mentions: List<String> = emptyList(),
    val postUrl: String? = null,
    val likes: Long = 0,
    val comments: Long = 0,
    val shares: Long = 0,
    val views: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class PostStatus {
    DRAFT,
    SCHEDULED,
    POSTED,
    FAILED
}

data class PostTemplate(
    val id: String = "",
    val name: String,
    val content: String,
    val platforms: List<SocialPlatform>,
    val hashtags: List<String> = emptyList(),
    val category: TemplateCategory = TemplateCategory.ANNOUNCEMENT
)

enum class TemplateCategory {
    ANNOUNCEMENT,
    TEASER,
    BEHIND_SCENES,
    THANK_YOU,
    COUNTDOWN,
    RELEASE_DAY,
    CUSTOM
}
