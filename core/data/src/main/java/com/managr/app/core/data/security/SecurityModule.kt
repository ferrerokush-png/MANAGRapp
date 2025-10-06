package com.managr.app.core.data.security

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Security Module for Dependency Injection
 * Provides all security-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {
    
    @Provides
    @Singleton
    fun provideEncryptionManager(): EncryptionManager {
        return EncryptionManager()
    }
    
    @Provides
    @Singleton
    fun provideSecurePreferences(
        @ApplicationContext context: Context
    ): SecurePreferences {
        return SecurePreferences(context)
    }
    
    @Provides
    @Singleton
    fun provideInputValidator(): InputValidator {
        return InputValidator()
    }
    
    @Provides
    @Singleton
    fun provideJwtTokenManager(
        securePreferences: SecurePreferences
    ): JwtTokenManager {
        return JwtTokenManager(securePreferences)
    }
    
    @Provides
    @Singleton
    fun provideOAuth2Manager(
        securePreferences: SecurePreferences,
        jwtTokenManager: JwtTokenManager
    ): OAuth2Manager {
        return OAuth2Manager(securePreferences, jwtTokenManager)
    }
    
    @Provides
    @Singleton
    fun provideCertificatePinningManager(): CertificatePinningManager {
        return CertificatePinningManager()
    }
    
    @Provides
    @Singleton
    fun provideSecurityLogger(): SecurityLogger {
        return SecurityLogger()
    }
    
    @Provides
    @Singleton
    fun provideSecurityHeadersInterceptor(): SecurityHeadersInterceptor {
        return SecurityHeadersInterceptor()
    }
    
    @Provides
    @Singleton
    fun provideApiKeyInterceptor(
        securePreferences: SecurePreferences
    ): ApiKeyInterceptor {
        return ApiKeyInterceptor(securePreferences)
    }
    
    @Provides
    @Singleton
    fun provideAuthTokenInterceptor(
        securePreferences: SecurePreferences
    ): AuthTokenInterceptor {
        return AuthTokenInterceptor(securePreferences)
    }
    
    @Provides
    @Singleton
    fun provideSecureOkHttpClient(
        certificatePinningManager: CertificatePinningManager,
        securityHeadersInterceptor: SecurityHeadersInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor,
        authTokenInterceptor: AuthTokenInterceptor
    ): OkHttpClient {
        return certificatePinningManager.createSecureClient()
            .newBuilder()
            .addInterceptor(securityHeadersInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(authTokenInterceptor)
            .build()
    }
}


