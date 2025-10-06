package com.managr.app.core.data.di

import android.content.Context
import com.managr.app.core.data.local.AppDatabase
import com.managr.app.core.data.local.dao.*
import com.managr.app.core.data.security.SecureDatabaseFactory
import com.managr.app.core.data.security.SecurePreferences
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
    fun provideSecureDatabaseFactory(
        securePreferences: SecurePreferences
    ): SecureDatabaseFactory {
        return SecureDatabaseFactory(securePreferences)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        secureDatabaseFactory: SecureDatabaseFactory
    ): AppDatabase {
        // Use encrypted database with SQLCipher for enterprise-grade security
        return secureDatabaseFactory.createEncryptedDatabase(
            context = context,
            databaseClass = AppDatabase::class.java,
            databaseName = "managr_encrypted_db"
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
