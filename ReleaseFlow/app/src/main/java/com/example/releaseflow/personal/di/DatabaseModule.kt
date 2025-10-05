package com.example.releaseflow.personal.di

import android.content.Context
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
import com.example.releaseflow.personal.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideProjectDao(db: AppDatabase): ProjectDao = db.projectDao()

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    @Singleton
    fun provideAnalyticsDao(db: AppDatabase): AnalyticsDao = db.analyticsDao()

    @Provides
    @Singleton
    fun provideContactDao(db: AppDatabase): ContactDao = db.contactDao()

    @Provides
    @Singleton
    fun provideAIQueryDao(db: AppDatabase): AIQueryDao = db.aiQueryDao()

    // Type converters
    @Provides
    @Singleton
    fun provideDateConverter(): DateConverter = DateConverter()

    @Provides
    @Singleton
    fun provideReleaseTypeConverter(): ReleaseTypeConverter = ReleaseTypeConverter()

    @Provides
    @Singleton
    fun provideProjectStatusConverter(): ProjectStatusConverter = ProjectStatusConverter()

    @Provides
    @Singleton
    fun provideTaskCategoryConverter(): TaskCategoryConverter = TaskCategoryConverter()

    @Provides
    @Singleton
    fun provideContactTypeConverter(): ContactTypeConverter = ContactTypeConverter()

    @Provides
    @Singleton
    fun provideAIQueryTypeConverter(): AIQueryTypeConverter = AIQueryTypeConverter()
}
