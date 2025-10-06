package com.managr.app.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.managr.app.core.data.local.converter.Converters
import com.managr.app.core.data.local.dao.*
import com.managr.app.core.data.local.entity.*

@Database(
    entities = [
        ProjectEntity::class,
        TaskEntity::class,
        StreamingMetricsEntity::class,
        SocialMediaMetricsEntity::class,
        RevenueEntity::class,
        CRMContactEntity::class,
        SubmissionEntity::class,
        DistributorEntity::class,
        AssetEntity::class,
        CalendarEventEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun analyticsDao(): AnalyticsDao
    abstract fun socialMediaDao(): SocialMediaDao
    abstract fun revenueDao(): RevenueDao
    abstract fun crmDao(): CRMDao
    abstract fun distributorDao(): DistributorDao
    abstract fun assetDao(): AssetDao
    abstract fun calendarDao(): CalendarDao
}
