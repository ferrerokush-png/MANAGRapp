package com.example.releaseflow.personal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "release_projects")
data class ReleaseProject(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "project_type")
    val projectType: String, // e.g., "Single", "EP", "Album"

    @ColumnInfo(name = "release_date")
    val releaseDate: Long, // store as epoch millis for simplicity

    @ColumnInfo(name = "artwork_uri")
    val artworkUri: String? = null,
)
