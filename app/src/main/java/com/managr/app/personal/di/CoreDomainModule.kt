package com.managr.app.personal.di

import com.managr.app.core.data.repository.CRMRepositoryImpl
import com.managr.app.core.data.repository.ProjectRepositoryImpl
import com.managr.app.core.data.repository.TaskRepositoryImpl
import com.managr.app.core.domain.repository.CRMRepository
import com.managr.app.core.domain.repository.ProjectRepository
import com.managr.app.core.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreDomainModule {

    @Binds
    @Singleton
    abstract fun bindCoreProjectRepository(
        projectRepositoryImpl: ProjectRepositoryImpl
    ): ProjectRepository

    @Binds
    @Singleton
    abstract fun bindCoreTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindCRMRepository(
        crmRepositoryImpl: CRMRepositoryImpl
    ): CRMRepository
}
