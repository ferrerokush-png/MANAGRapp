package com.example.releaseflow.personal.di

import com.example.releaseflow.personal.data.repository.AnalyticsRepositoryInterfaceImpl
import com.example.releaseflow.personal.data.repository.ContactRepositoryInterfaceImpl
import com.example.releaseflow.personal.domain.repository.AnalyticsRepository
import com.example.releaseflow.personal.domain.repository.ContactRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsRepository(
        analyticsRepositoryInterfaceImpl: AnalyticsRepositoryInterfaceImpl
    ): AnalyticsRepository

    @Binds
    @Singleton
    abstract fun bindContactRepository(
        contactRepositoryInterfaceImpl: ContactRepositoryInterfaceImpl
    ): ContactRepository
}
