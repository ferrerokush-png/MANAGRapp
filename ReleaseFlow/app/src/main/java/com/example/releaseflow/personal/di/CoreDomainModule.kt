package com.example.releaseflow.personal.di

import com.example.releaseflow.core.data.repository.CRMRepositoryImpl
import com.example.releaseflow.core.domain.repository.CRMRepository
import com.example.releaseflow.core.domain.repository.ProjectRepository as CoreProjectRepository
import com.example.releaseflow.core.domain.repository.TaskRepository as CoreTaskRepository
import com.example.releaseflow.personal.data.repository.ProjectRepositoryImpl
import com.example.releaseflow.personal.data.repository.TaskRepositoryImpl
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
    ): CoreProjectRepository

    @Binds
    @Singleton
    abstract fun bindCoreTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): CoreTaskRepository

    @Binds
    @Singleton
    abstract fun bindCRMRepository(
        crmRepositoryImpl: CRMRepositoryImpl
    ): CRMRepository
}
