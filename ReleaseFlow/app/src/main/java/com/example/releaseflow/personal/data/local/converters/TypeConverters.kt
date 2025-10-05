package com.example.releaseflow.personal.data.local.converters

import androidx.room.TypeConverter
import com.example.releaseflow.personal.data.local.entity.ContactType
import com.example.releaseflow.personal.data.local.entity.ProjectStatus
import com.example.releaseflow.personal.data.local.entity.ReleaseType
import com.example.releaseflow.personal.data.local.entity.TaskCategory
import java.util.Date

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

class ReleaseTypeConverter {
    @TypeConverter
    fun fromReleaseType(value: ReleaseType): String {
        return value.name
    }

    @TypeConverter
    fun toReleaseType(value: String): ReleaseType {
        return ReleaseType.valueOf(value)
    }
}

class ProjectStatusConverter {
    @TypeConverter
    fun fromProjectStatus(value: ProjectStatus): String {
        return value.name
    }

    @TypeConverter
    fun toProjectStatus(value: String): ProjectStatus {
        return ProjectStatus.valueOf(value)
    }
}

class TaskCategoryConverter {
    @TypeConverter
    fun fromTaskCategory(value: TaskCategory): String {
        return value.name
    }

    @TypeConverter
    fun toTaskCategory(value: String): TaskCategory {
        return TaskCategory.valueOf(value)
    }
}

class ContactTypeConverter {
    @TypeConverter
    fun fromContactType(value: ContactType): String {
        return value.name
    }

    @TypeConverter
    fun toContactType(value: String): ContactType {
        return ContactType.valueOf(value)
    }
}
