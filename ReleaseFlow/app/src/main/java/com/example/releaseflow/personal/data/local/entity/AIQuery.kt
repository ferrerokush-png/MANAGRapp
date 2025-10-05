package com.example.releaseflow.personal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

enum class AIQueryType {
    ANALYTICS_INSIGHTS,
    TASK_SUGGESTIONS,
    PLAYLIST_PITCH,
    RELEASE_STRATEGY
}

@Entity(tableName = "ai_queries")
data class AIQuery(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "query_type")
    val queryType: AIQueryType,

    @ColumnInfo(name = "prompt")
    val prompt: String,

    @ColumnInfo(name = "response")
    val response: String,

    @ColumnInfo(name = "project_id")
    val projectId: Long? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "helpful")
    val helpful: Boolean? = null
)
