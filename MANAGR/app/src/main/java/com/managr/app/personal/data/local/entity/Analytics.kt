package com.managr.app.personal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "analytics",
    foreignKeys = [
        ForeignKey(
            entity = ReleaseProject::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["project_id"])]
)
data class Analytics(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "project_id")
    val projectId: Long,

    @ColumnInfo(name = "platform")
    val platform: String,

    @ColumnInfo(name = "metric_type")
    val metricType: String,

    @ColumnInfo(name = "value")
    val value: Double,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "additional_data")
    val additionalData: String? = null
)
