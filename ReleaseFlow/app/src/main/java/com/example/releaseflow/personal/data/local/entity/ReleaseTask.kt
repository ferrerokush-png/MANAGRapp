package com.example.releaseflow.personal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

enum class TaskCategory {
    PRODUCTION, MARKETING, DISTRIBUTION
}

@Entity(
    tableName = "release_tasks",
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
data class ReleaseTask(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "project_id")
    val projectId: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "due_date")
    val dueDate: Date,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "priority")
    val priority: Int = 2,

    @ColumnInfo(name = "category")
    val category: TaskCategory,

    @ColumnInfo(name = "assigned_to")
    val assignedTo: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)
