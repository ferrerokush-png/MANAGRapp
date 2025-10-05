package com.example.releaseflow.personal.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.releaseflow.personal.data.local.converters.AIQueryTypeConverter
import com.example.releaseflow.personal.data.local.converters.ContactTypeConverter
import com.example.releaseflow.personal.data.local.converters.DateConverter
import com.example.releaseflow.personal.data.local.converters.ProjectStatusConverter
import com.example.releaseflow.personal.data.local.converters.ReleaseTypeConverter
import com.example.releaseflow.personal.data.local.converters.TaskCategoryConverter
import com.example.releaseflow.personal.data.local.dao.AIQueryDao
import com.example.releaseflow.personal.data.local.dao.AnalyticsDao
import com.example.releaseflow.personal.data.local.dao.ContactDao
import com.example.releaseflow.personal.data.local.dao.ProjectDao
import com.example.releaseflow.personal.data.local.dao.TaskDao
import com.example.releaseflow.personal.data.local.entity.AIQuery
import com.example.releaseflow.personal.data.local.entity.Analytics
import com.example.releaseflow.personal.data.local.entity.Contact
import com.example.releaseflow.personal.data.local.entity.ReleaseProject
import com.example.releaseflow.personal.data.local.entity.ReleaseTask

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

        // Migration from version 1 to 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add new columns to ReleaseProject table
                database.execSQL("ALTER TABLE ReleaseProject ADD COLUMN completionPercentage INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE ReleaseProject ADD COLUMN completedTasks INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE ReleaseProject ADD COLUMN totalTasks INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "release_flow.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}
