package com.example.releaseflow.core.data.di

import android.content.Context
import androidx.room.Room
import com.example.releaseflow.core.data.local.AppDatabase
import com.example.releaseflow.core.data.local.dao.*
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "release_flow_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideProjectDao(database: AppDatabase): ProjectDao = database.projectDao()

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()

    @Provides
    fun provideAnalyticsDao(database: AppDatabase): AnalyticsDao = database.analyticsDao()

    @Provides
    fun provideSocialMediaDao(database: AppDatabase): SocialMediaDao = database.socialMediaDao()

    @Provides
    fun provideRevenueDao(database: AppDatabase): RevenueDao = database.revenueDao()

    @Provides
    fun provideCRMDao(database: AppDatabase): CRMDao = database.crmDao()

    @Provides
    fun provideDistributorDao(database: AppDatabase): DistributorDao = database.distributorDao()

    @Provides
    fun provideAssetDao(database: AppDatabase): AssetDao = database.assetDao()

    @Provides
    fun provideCalendarDao(database: AppDatabase): CalendarDao = database.calendarDao()

    // Repository providers moved to personal module to avoid duplicate bindings

    // Use case providers moved to personal module to avoid duplicate bindings
}
