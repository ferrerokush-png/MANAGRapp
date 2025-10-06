package com.managr.app.personal.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

// Core release types
enum class ReleaseType { SINGLE, EP, ALBUM }

// Project aggregate
data class Project(
    val id: String,
    val title: String,
    val type: ReleaseType,
    val releaseDate: LocalDate,
    val tracks: List<Track> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val assets: List<Asset> = emptyList(),
    val campaigns: List<Campaign> = emptyList(),
    val contacts: List<Contact> = emptyList(),
    val distributorProfile: DistributorProfile? = null,
    val metadata: Map<String, String> = emptyMap(),
)

// Track information
data class Track(
    val id: String,
    val title: String,
    val durationMs: Long? = null,
    val isrc: String? = null,
    val explicit: Boolean = false,
    val artworkAssetId: String? = null,
)

// Tasks and dependencies
enum class TaskStatus { PENDING, IN_PROGRESS, COMPLETED }

data class Task(
    val id: String,
    val projectId: String,
    val title: String,
    val due: LocalDate,
    val status: TaskStatus = TaskStatus.PENDING,
    val notes: String = "",
    val subtasks: List<SubTask> = emptyList(),
    val dependencies: List<Dependency> = emptyList(),
)

data class SubTask(
    val id: String,
    val parentTaskId: String,
    val title: String,
    val done: Boolean = false,
)

enum class DependencyType { BLOCKS, RELATES }

data class Dependency(
    val id: String,
    val fromTaskId: String, // prerequisite
    val toTaskId: String,   // dependent
    val type: DependencyType = DependencyType.BLOCKS,
    val lagDays: Long = 0,
)

// Assets
enum class AssetType { ARTWORK, AUDIO, VIDEO, DOCUMENT, OTHER }

data class Asset(
    val id: String,
    val projectId: String,
    val type: AssetType,
    val uri: String,
    val thumbnailUri: String? = null,
    val createdAt: LocalDate? = null,
    val tags: Set<String> = emptySet(),
)

// Promotions & CRM
enum class SocialPlatform { TIKTOK, INSTAGRAM, YOUTUBE, TWITTER, FACEBOOK, OTHER }

enum class PostStatus { DRAFT, SCHEDULED, SENT, FAILED }

data class SocialPost(
    val id: String,
    val campaignId: String,
    val platform: SocialPlatform,
    val scheduledAt: LocalDateTime? = null,
    val caption: String = "",
    val mediaAssetIds: List<String> = emptyList(),
    val status: PostStatus = PostStatus.DRAFT,
)

data class Campaign(
    val id: String,
    val projectId: String,
    val name: String,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val posts: List<SocialPost> = emptyList(),
)

enum class ContactType { CURATOR, BLOGGER, RADIO, OTHER }

data class Contact(
    val id: String,
    val name: String,
    val organization: String? = null,
    val email: String? = null,
    val url: String? = null,
    val tags: Set<String> = emptySet(),
    val lastOutreach: LocalDate? = null,
    val notes: String = "",
    val type: ContactType = ContactType.OTHER,
)

// Calendar & Analytics
enum class EventType { RELEASE, UPLOAD_BY, TASK_DUE, MILESTONE, CAMPAIGN_POST }

data class CalendarEvent(
    val id: String,
    val projectId: String,
    val date: LocalDate,
    val type: EventType,
    val title: String,
    val description: String = "",
    val refId: String? = null,
)

enum class AnalyticsPlatform { SPOTIFY, APPLE_MUSIC, TIKTOK, INSTAGRAM, YOUTUBE, OTHER }

data class MetricSnapshot(
    val id: String,
    val date: LocalDate,
    val platform: AnalyticsPlatform,
    val values: Map<String, Double> = emptyMap(),
)

// Revenue
enum class RevenueSource { SPOTIFY, APPLE_MUSIC, BANDCAMP, MERCH, YOUTUBE, OTHER }

data class RevenueRecord(
    val id: String,
    val date: LocalDate,
    val source: RevenueSource,
    val amount: Double,
    val currency: String = "USD",
    val trackId: String? = null,
    val notes: String? = null,
)

// Distributor policy/profile
enum class WeekendAdjust { None, PreviousBusinessDay, NextBusinessDay }

data class DistributorProfile(
    val id: String,
    val name: String,
    val defaultLeadDays: Long = 21,
    val notes: String? = null,
    val weekendAdjust: WeekendAdjust = WeekendAdjust.None,
)

// Templates

data class TaskTemplate(
    val title: String,
    val offsetDaysFromRelease: Long,
    val notes: String = "",
)

data class Template(
    val id: String,
    val name: String,
    val type: ReleaseType,
    val tasks: List<TaskTemplate> = emptyList(),
    val defaultLeadDays: Long = 21,
)
