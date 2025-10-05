package com.example.releaseflow.personal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

enum class ContactType {
    CURATOR, BLOGGER, RADIO, LABEL
}

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: ContactType,

    @ColumnInfo(name = "email")
    val email: String? = null,

    @ColumnInfo(name = "platform")
    val platform: String? = null,

    @ColumnInfo(name = "genre_focus")
    val genreFocus: String? = null,

    @ColumnInfo(name = "submission_url")
    val submissionUrl: String? = null,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "last_contact")
    val lastContact: Date? = null,

    @ColumnInfo(name = "success_rate")
    val successRate: Float? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)
