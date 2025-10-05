package com.example.releaseflow.core.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for data layer dependencies
 * Provides database, repositories, and data sources
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // Database and repository providers will be added here
    // This is a placeholder for the multi-module setup
}
