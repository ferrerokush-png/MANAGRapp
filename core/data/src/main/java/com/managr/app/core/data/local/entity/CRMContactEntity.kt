package com.managr.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.managr.app.core.domain.model.*
import java.time.LocalDate

@Entity(
    tableName = "crm_contacts",
    indices = [Index("type"), Index("lastContactDate")]
)
data class CRMContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: String, // ContactType as String
    val email: String?,
    val phone: String?,
    val company: String?,
    val platform: String?, // SocialPlatform as String
    val socialHandle: String?,
    val website: String?,
    val genres: List<String>,
    val playlistName: String?,
    val playlistFollowers: Long?,
    val playlistUrl: String?,
    val relationshipStrength: String, // RelationshipStrength as String
    val lastContactDate: LocalDate?,
    val nextFollowUpDate: LocalDate?,
    val notes: String,
    val tags: List<String>,
    val submissionHistory: List<Long>,
    val responseRate: Float,
    val isActive: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

fun CRMContactEntity.toDomain(): CRMContact {
    return CRMContact(
        id = id,
        name = name,
        type = ContactType.valueOf(type),
        email = email,
        phone = phone,
        company = company,
        platform = platform?.let { SocialPlatform.valueOf(it) },
        socialHandle = socialHandle,
        website = website,
        genres = genres,
        playlistName = playlistName,
        playlistFollowers = playlistFollowers,
        playlistUrl = playlistUrl,
        relationshipStrength = RelationshipStrength.valueOf(relationshipStrength),
        lastContactDate = lastContactDate,
        nextFollowUpDate = nextFollowUpDate,
        notes = notes,
        tags = tags,
        submissionHistory = submissionHistory,
        responseRate = responseRate,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CRMContact.toEntity(): CRMContactEntity {
    return CRMContactEntity(
        id = id,
        name = name,
        type = type.name,
        email = email,
        phone = phone,
        company = company,
        platform = platform?.name,
        socialHandle = socialHandle,
        website = website,
        genres = genres,
        playlistName = playlistName,
        playlistFollowers = playlistFollowers,
        playlistUrl = playlistUrl,
        relationshipStrength = relationshipStrength.name,
        lastContactDate = lastContactDate,
        nextFollowUpDate = nextFollowUpDate,
        notes = notes,
        tags = tags,
        submissionHistory = submissionHistory,
        responseRate = responseRate,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
