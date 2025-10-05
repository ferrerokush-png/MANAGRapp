package com.example.releaseflow.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.releaseflow.core.domain.model.AssetType
import com.example.releaseflow.core.domain.model.PromotionalAsset

@Entity(
    tableName = "promotional_assets",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId"), Index("type")]
)
data class AssetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val name: String,
    val type: String, // AssetType as String
    val fileUri: String,
    val fileSize: Long,
    val mimeType: String?,
    val description: String,
    val tags: List<String>,
    val isPublic: Boolean,
    val downloadUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)

fun AssetEntity.toDomain(): PromotionalAsset {
    return PromotionalAsset(
        id = id,
        projectId = projectId,
        name = name,
        type = AssetType.valueOf(type),
        fileUri = fileUri,
        fileSize = fileSize,
        mimeType = mimeType,
        description = description,
        tags = tags,
        isPublic = isPublic,
        downloadUrl = downloadUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun PromotionalAsset.toEntity(): AssetEntity {
    return AssetEntity(
        id = id,
        projectId = projectId,
        name = name,
        type = type.name,
        fileUri = fileUri,
        fileSize = fileSize,
        mimeType = mimeType,
        description = description,
        tags = tags,
        isPublic = isPublic,
        downloadUrl = downloadUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
