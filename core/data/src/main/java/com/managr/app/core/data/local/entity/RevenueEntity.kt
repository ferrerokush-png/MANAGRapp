package com.managr.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.managr.app.core.domain.model.Revenue
import com.managr.app.core.domain.model.StreamingPlatform
import java.time.LocalDate
import java.time.YearMonth

/**
 * Room entity for Revenue
 */
@Entity(
    tableName = "revenue",
    indices = [Index("projectId"), Index("platform"), Index("period")]
)
data class RevenueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long?,
    val platform: String, // StreamingPlatform as String
    val period: YearMonth,
    val streams: Long,
    val amount: Double,
    val currency: String,
    val payoutDate: LocalDate?,
    val isPaid: Boolean,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * Map RevenueEntity to domain Revenue
 */
fun RevenueEntity.toDomain(): Revenue {
    return Revenue(
        id = id,
        projectId = projectId,
        platform = StreamingPlatform.valueOf(platform),
        period = period,
        streams = streams,
        amount = amount,
        currency = currency,
        payoutDate = payoutDate,
        isPaid = isPaid,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

/**
 * Map domain Revenue to RevenueEntity
 */
fun Revenue.toEntity(): RevenueEntity {
    return RevenueEntity(
        id = id,
        projectId = projectId,
        platform = platform.name,
        period = period,
        streams = streams,
        amount = amount,
        currency = currency,
        payoutDate = payoutDate,
        isPaid = isPaid,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
