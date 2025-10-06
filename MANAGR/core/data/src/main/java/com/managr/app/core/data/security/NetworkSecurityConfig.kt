package com.managr.app.core.data.security

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Network Security Interceptor
 * Adds security headers and validates responses
 */
@Singleton
class SecurityHeadersInterceptor @Inject constructor() : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            // Add security headers
            .addHeader("X-Content-Type-Options", "nosniff")
            .addHeader("X-Frame-Options", "DENY")
            .addHeader("X-XSS-Protection", "1; mode=block")
            .addHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
            .build()
        
        val response = chain.proceed(request)
        
        // Validate response headers
        validateResponseSecurity(response)
        
        return response
    }
    
    private fun validateResponseSecurity(response: Response) {
        // Check for insecure content
        if (!response.request.isHttps) {
            throw SecurityException("Insecure HTTP connection detected. Only HTTPS is allowed.")
        }
        
        // Additional security validations can be added here
        // For example: check for specific security headers in response
    }
}

/**
 * API Key Interceptor
 * Securely adds API keys to requests
 */
@Singleton
class ApiKeyInterceptor @Inject constructor(
    private val securePreferences: SecurePreferences
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = securePreferences.getString(SecurePreferences.KEY_API_KEY)
        
        val request = if (!apiKey.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
                .build()
        } else {
            chain.request()
        }
        
        return chain.proceed(request)
    }
}

/**
 * Auth Token Interceptor
 * Adds authentication tokens to requests
 */
@Singleton
class AuthTokenInterceptor @Inject constructor(
    private val securePreferences: SecurePreferences
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = securePreferences.getString(SecurePreferences.KEY_AUTH_TOKEN)
        
        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        
        val response = chain.proceed(request)
        
        // Check for 401 Unauthorized
        if (response.code == 401) {
            // Token expired, attempt refresh
            handleTokenExpiration()
        }
        
        return response
    }
    
    private fun handleTokenExpiration() {
        // Implement token refresh logic
        // This should trigger a token refresh flow
        securePreferences.remove(SecurePreferences.KEY_AUTH_TOKEN)
    }
}


