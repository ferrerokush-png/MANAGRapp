package com.example.releaseflow.personal.di

import android.content.Context
import com.example.releaseflow.personal.data.local.dao.AppDao
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
    fun provideAppDao(db: AppDatabase): AppDao = db.appDao()
}
