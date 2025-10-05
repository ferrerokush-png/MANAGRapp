package com.example.releaseflow.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.releaseflow.core.domain.model.Distributor
import com.example.releaseflow.core.domain.model.DistributorType
import com.example.releaseflow.core.domain.model.UploadStatus
import java.time.LocalDate

@Entity(
    tableName = "distributors",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId"), Index("uploadDeadline")]
)
data class DistributorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val type: String, // DistributorType as String
    val accountEmail: String?,
    val uploadDeadline: LocalDate,
    val uploadedDate: LocalDate?,
    val uploadStatus: String, // UploadStatus as String
    val releaseUrl: String?,
    val upc: String?,
    val isrc: String?,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)

fun DistributorEntity.toDomain(): Distributor {
    return Distributor(
        id = id,
        projectId = projectId,
        type = DistributorType.valueOf(type),
        accountEmail = accountEmail,
        uploadDeadline = uploadDeadline,
        uploadedDate = uploadedDate,
        uploadStatus = UploadStatus.valueOf(uploadStatus),
        releaseUrl = releaseUrl,
        upc = upc,
        isrc = isrc,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Distributor.toEntity(): DistributorEntity {
    return DistributorEntity(
        id = id,
        projectId = projectId,
        type = type.name,
        accountEmail = accountEmail,
        uploadDeadline = uploadDeadline,
        uploadedDate = uploadedDate,
        uploadStatus = uploadStatus.name,
        releaseUrl = releaseUrl,
        upc = upc,
        isrc = isrc,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
