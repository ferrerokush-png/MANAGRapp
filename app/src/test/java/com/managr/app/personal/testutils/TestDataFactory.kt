package com.managr.app.personal.testutils

import com.managr.app.core.domain.model.*
import java.time.LocalDate
import java.time.LocalDateTime

object TestDataFactory {
    
    fun createProject(
        id: Long = 1L,
        title: String = "Test Project",
        type: ReleaseType = ReleaseType.SINGLE,
        status: ProjectStatus = ProjectStatus.IN_PROGRESS,
        releaseDate: LocalDate = LocalDate.now().plusDays(30),
        distributorType: DistributorType = DistributorType.DISTROKID,
        trackCount: Int = 1,
        completedTasks: Int = 0,
        totalTasks: Int = 10,
        completionPercentage: Float = 0f,
        artworkUri: String? = null,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): Project {
        return Project(
            id = id,
            title = title,
            type = type,
            status = status,
            releaseDate = releaseDate,
            distributorType = distributorType,
            trackCount = trackCount,
            completedTasks = completedTasks,
            totalTasks = totalTasks,
            completionPercentage = completionPercentage,
            artworkUri = artworkUri,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    fun createTask(
        id: Long = 1L,
        projectId: Long = 1L,
        title: String = "Test Task",
        description: String = "Test task description",
        phase: TaskPhase = TaskPhase.PRE_PRODUCTION,
        status: TaskStatus = TaskStatus.PENDING,
        dueDate: LocalDate = LocalDate.now().plusDays(7),
        order: Int = 0,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): Task {
        return Task(
            id = id,
            projectId = projectId,
            title = title,
            description = description,
            phase = phase,
            status = status,
            dueDate = dueDate,
            order = order,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    fun createStreamingMetrics(
        id: Long = 1L,
        projectId: Long = 1L,
        platform: StreamingPlatform = StreamingPlatform.SPOTIFY,
        streams: Long = 1000,
        listeners: Long = 500,
        date: LocalDate = LocalDate.now(),
        createdAt: LocalDateTime = LocalDateTime.now()
    ): StreamingMetrics {
        return StreamingMetrics(
            id = id,
            projectId = projectId,
            platform = platform,
            streams = streams,
            listeners = listeners,
            date = date,
            createdAt = createdAt
        )
    }
    
    fun createSocialMediaMetrics(
        id: Long = 1L,
        projectId: Long = 1L,
        platform: SocialPlatform = SocialPlatform.INSTAGRAM,
        followers: Long = 1000,
        likes: Long = 100,
        shares: Long = 50,
        comments: Long = 25,
        date: LocalDate = LocalDate.now(),
        createdAt: LocalDateTime = LocalDateTime.now()
    ): SocialMediaMetrics {
        return SocialMediaMetrics(
            id = id,
            projectId = projectId,
            platform = platform,
            followers = followers,
            likes = likes,
            shares = shares,
            comments = comments,
            date = date,
            createdAt = createdAt
        )
    }
    
    fun createRevenue(
        id: Long = 1L,
        projectId: Long = 1L,
        platform: StreamingPlatform = StreamingPlatform.SPOTIFY,
        amount: Double = 10.50,
        currency: String = "USD",
        date: LocalDate = LocalDate.now(),
        createdAt: LocalDateTime = LocalDateTime.now()
    ): Revenue {
        return Revenue(
            id = id,
            projectId = projectId,
            platform = platform,
            amount = amount,
            currency = currency,
            date = date,
            createdAt = createdAt
        )
    }
    
    fun createCRMContact(
        id: Long = 1L,
        name: String = "Test Contact",
        email: String = "test@example.com",
        type: ContactType = ContactType.PLAYLIST_CURATOR,
        platform: String = "Spotify",
        genre: String = "Pop",
        relationshipStrength: RelationshipStrength = RelationshipStrength.MEDIUM,
        lastContactDate: LocalDate? = null,
        notes: String? = null,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): CRMContact {
        return CRMContact(
            id = id,
            name = name,
            email = email,
            type = type,
            platform = platform,
            genre = genre,
            relationshipStrength = relationshipStrength,
            lastContactDate = lastContactDate,
            notes = notes,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    fun createDistributor(
        id: Long = 1L,
        name: String = "Test Distributor",
        type: DistributorType = DistributorType.DISTROKID,
        leadDays: Long = 7,
        isActive: Boolean = true,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): Distributor {
        return Distributor(
            id = id,
            name = name,
            type = type,
            leadDays = leadDays,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    fun createPromotionalAsset(
        id: Long = 1L,
        projectId: Long = 1L,
        type: AssetType = AssetType.ARTWORK,
        title: String = "Test Asset",
        description: String = "Test asset description",
        filePath: String = "/path/to/asset.jpg",
        fileSize: Long = 1024000,
        mimeType: String = "image/jpeg",
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): PromotionalAsset {
        return PromotionalAsset(
            id = id,
            projectId = projectId,
            type = type,
            title = title,
            description = description,
            filePath = filePath,
            fileSize = fileSize,
            mimeType = mimeType,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    fun createCalendarEvent(
        id: Long = 1L,
        projectId: Long = 1L,
        title: String = "Test Event",
        description: String = "Test event description",
        type: EventType = EventType.RELEASE_DATE,
        date: LocalDate = LocalDate.now().plusDays(7),
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): CalendarEvent {
        return CalendarEvent(
            id = id,
            projectId = projectId,
            title = title,
            description = description,
            type = type,
            date = date,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    fun createSubmission(
        id: Long = 1L,
        projectId: Long = 1L,
        contactId: Long = 1L,
        platform: String = "Spotify",
        status: SubmissionStatus = SubmissionStatus.PENDING,
        submittedAt: LocalDateTime? = null,
        responseDate: LocalDate? = null,
        notes: String? = null,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): Submission {
        return Submission(
            id = id,
            projectId = projectId,
            contactId = contactId,
            platform = platform,
            status = status,
            submittedAt = submittedAt,
            responseDate = responseDate,
            notes = notes,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    fun createProjectsList(count: Int = 5): List<Project> {
        return (1..count).map { index ->
            createProject(
                id = index.toLong(),
                title = "Test Project $index",
                type = ReleaseType.values().random(),
                status = ProjectStatus.values().random()
            )
        }
    }
    
    fun createTasksList(projectId: Long, count: Int = 5): List<Task> {
        return (1..count).map { index ->
            createTask(
                id = index.toLong(),
                projectId = projectId,
                title = "Test Task $index",
                phase = TaskPhase.values().random(),
                status = TaskStatus.values().random()
            )
        }
    }
    
    fun createStreamingMetricsList(projectId: Long, count: Int = 5): List<StreamingMetrics> {
        return (1..count).map { index ->
            createStreamingMetrics(
                id = index.toLong(),
                projectId = projectId,
                platform = StreamingPlatform.values().random(),
                streams = (1000..10000).random().toLong(),
                listeners = (100..1000).random().toLong()
            )
        }
    }
    
    fun createSocialMediaMetricsList(projectId: Long, count: Int = 5): List<SocialMediaMetrics> {
        return (1..count).map { index ->
            createSocialMediaMetrics(
                id = index.toLong(),
                projectId = projectId,
                platform = SocialPlatform.values().random(),
                followers = (1000..10000).random().toLong(),
                likes = (100..1000).random().toLong()
            )
        }
    }
    
    fun createRevenueList(projectId: Long, count: Int = 5): List<Revenue> {
        return (1..count).map { index ->
            createRevenue(
                id = index.toLong(),
                projectId = projectId,
                platform = StreamingPlatform.values().random(),
                amount = (1.0..100.0).random()
            )
        }
    }
    
    fun createCRMContactsList(count: Int = 5): List<CRMContact> {
        return (1..count).map { index ->
            createCRMContact(
                id = index.toLong(),
                name = "Test Contact $index",
                email = "test$index@example.com",
                type = ContactType.values().random(),
                platform = listOf("Spotify", "Apple Music", "YouTube").random()
            )
        }
    }
}
