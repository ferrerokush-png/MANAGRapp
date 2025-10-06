package com.managr.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.managr.app.core.domain.model.Submission
import com.managr.app.core.domain.model.SubmissionStatus
import java.time.LocalDate

@Entity(
    tableName = "submissions",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CRMContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId"), Index("contactId"), Index("status")]
)
data class SubmissionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val contactId: Long,
    val submissionDate: LocalDate,
    val status: String, // SubmissionStatus as String
    val response: String,
    val responseDate: LocalDate?,
    val followUpDate: LocalDate?,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)

fun SubmissionEntity.toDomain(): Submission {
    return Submission(
        id = id,
        projectId = projectId,
        contactId = contactId,
        submissionDate = submissionDate,
        status = SubmissionStatus.valueOf(status),
        response = response,
        responseDate = responseDate,
        followUpDate = followUpDate,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Submission.toEntity(): SubmissionEntity {
    return SubmissionEntity(
        id = id,
        projectId = projectId,
        contactId = contactId,
        submissionDate = submissionDate,
        status = status.name,
        response = response,
        responseDate = responseDate,
        followUpDate = followUpDate,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
