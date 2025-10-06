package com.managr.app.personal.di

import com.managr.app.personal.data.repository.AnalyticsRepositoryInterfaceImpl
import com.managr.app.personal.data.repository.ContactRepositoryInterfaceImpl
import com.managr.app.personal.domain.repository.AnalyticsRepository
import com.managr.app.personal.domain.repository.ContactRepository
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
