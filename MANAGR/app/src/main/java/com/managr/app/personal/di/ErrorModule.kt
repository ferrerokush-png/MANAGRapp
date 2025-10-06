package com.managr.app.personal.di

import com.managr.app.core.design.error.ErrorHandler
import com.managr.app.personal.error.GlobalExceptionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorModule {
    
    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler {
        return ErrorHandler()
    }
    
    @Provides
    @Singleton
    fun provideGlobalExceptionHandler(
        errorHandler: ErrorHandler
    ): GlobalExceptionHandler {
        return GlobalExceptionHandler(errorHandler)
    }
}
