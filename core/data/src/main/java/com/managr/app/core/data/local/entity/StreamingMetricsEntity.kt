package com.managr.app.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.managr.app.core.domain.model.StreamingMetrics
import com.managr.app.core.domain.model.StreamingPlatform
import java.time.LocalDate

/**
 * Room entity for Streaming Metrics
 */
@Entity(
    tableName = "streaming_metrics",
    indices = [Index("projectId"), Index("platform"), Index("date")]
)
data class StreamingMetricsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long?,
    val trackId: Long?,
    val platform: String, // StreamingPlatform as String
    val date: LocalDate,
    val streams: Long,
    val listeners: Long,
    val saves: Long,
    val playlistAdds: Long,
    val skipRate: Float,
    val completionRate: Float,
    val averageListenDuration: Int,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * Map StreamingMetricsEntity to domain StreamingMetrics
 */
fun StreamingMetricsEntity.toDomain(): StreamingMetrics {
    return StreamingMetrics(
        id = id,
        projectId = projectId,
        trackId = trackId,
        platform = StreamingPlatform.valueOf(platform),
        date = date,
        streams = streams,
        listeners = listeners,
        saves = saves,
        playlistAdds = playlistAdds,
        skipRate = skipRate,
        completionRate = completionRate,
        averageListenDuration = averageListenDuration,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

/**
 * Map domain StreamingMetrics to StreamingMetricsEntity
 */
fun StreamingMetrics.toEntity(): StreamingMetricsEntity {
    return StreamingMetricsEntity(
        id = id,
        projectId = projectId,
        trackId = trackId,
        platform = platform.name,
        date = date,
        streams = streams,
        listeners = listeners,
        saves = saves,
        playlistAdds = playlistAdds,
        skipRate = skipRate,
        completionRate = completionRate,
        averageListenDuration = averageListenDuration,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
