package com.managr.app.personal.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.managr.app.personal.data.local.converters.AIQueryTypeConverter
import com.managr.app.personal.data.local.converters.ContactTypeConverter
import com.managr.app.personal.data.local.converters.DateConverter
import com.managr.app.personal.data.local.converters.ProjectStatusConverter
import com.managr.app.personal.data.local.converters.ReleaseTypeConverter
import com.managr.app.personal.data.local.converters.TaskCategoryConverter
import com.managr.app.personal.data.local.dao.AIQueryDao
import com.managr.app.personal.data.local.dao.AnalyticsDao
import com.managr.app.personal.data.local.dao.ContactDao
import com.managr.app.personal.data.local.dao.ProjectDao
import com.managr.app.personal.data.local.dao.TaskDao
import com.managr.app.personal.data.local.entity.AIQuery
import com.managr.app.personal.data.local.entity.Analytics
import com.managr.app.personal.data.local.entity.Contact
import com.managr.app.personal.data.local.entity.ReleaseProject
import com.managr.app.personal.data.local.entity.ReleaseTask

@Database(
    entities = [
        ReleaseProject::class,
        ReleaseTask::class,
        Analytics::class,
        Contact::class,
        AIQuery::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    ReleaseTypeConverter::class,
    ProjectStatusConverter::class,
    TaskCategoryConverter::class,
    ContactTypeConverter::class,
    AIQueryTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun analyticsDao(): AnalyticsDao
    abstract fun contactDao(): ContactDao
    abstract fun aiQueryDao(): AIQueryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "release_flow.db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}
