package com.managr.app.core.data.security

import android.util.Base64
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * JWT Token Manager
 * Handles JWT token validation, parsing, and refresh
 */
@Singleton
class JwtTokenManager @Inject constructor(
    private val securePreferences: SecurePreferences
) {
    
    /**
     * Save access and refresh tokens securely
     */
    fun saveTokens(accessToken: String, refreshToken: String) {
        securePreferences.putString(SecurePreferences.KEY_AUTH_TOKEN, accessToken)
        securePreferences.putString(SecurePreferences.KEY_REFRESH_TOKEN, refreshToken)
        securePreferences.putLong(SecurePreferences.KEY_LAST_AUTH_TIME, System.currentTimeMillis())
    }
    
    /**
     * Get access token
     */
    fun getAccessToken(): String? {
        return securePreferences.getString(SecurePreferences.KEY_AUTH_TOKEN)
    }
    
    /**
     * Get refresh token
     */
    fun getRefreshToken(): String? {
        return securePreferences.getString(SecurePreferences.KEY_REFRESH_TOKEN)
    }
    
    /**
     * Clear all tokens
     */
    fun clearTokens() {
        securePreferences.remove(SecurePreferences.KEY_AUTH_TOKEN)
        securePreferences.remove(SecurePreferences.KEY_REFRESH_TOKEN)
        securePreferences.remove(SecurePreferences.KEY_LAST_AUTH_TIME)
    }
    
    /**
     * Check if token is expired
     */
    fun isTokenExpired(token: String): Boolean {
        return try {
            val expirationTime = getTokenExpiration(token)
            Date().time >= expirationTime
        } catch (e: Exception) {
            true // Assume expired if we can't parse
        }
    }
    
    /**
     * Get token expiration time
     */
    fun getTokenExpiration(token: String): Long {
        val payload = decodePayload(token)
        val exp = payload.optLong("exp", 0)
        return exp * 1000 // Convert to milliseconds
    }
    
    /**
     * Parse JWT token and extract claims
     */
    fun parseToken(token: String): JwtClaims {
        val payload = decodePayload(token)
        
        return JwtClaims(
            subject = payload.optString("sub", ""),
            issuer = payload.optString("iss", ""),
            audience = payload.optString("aud", ""),
            expiration = payload.optLong("exp", 0),
            issuedAt = payload.optLong("iat", 0),
            userId = payload.optString("user_id", ""),
            email = payload.optString("email", ""),
            roles = payload.optJSONArray("roles")?.let { array ->
                List(array.length()) { array.getString(it) }
            } ?: emptyList()
        )
    }
    
    /**
     * Decode JWT payload
     */
    private fun decodePayload(token: String): JSONObject {
        val parts = token.split(".")
        if (parts.size != 3) {
            throw IllegalArgumentException("Invalid JWT token format")
        }
        
        val payload = parts[1]
        val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        return JSONObject(String(decoded))
    }
    
    /**
     * Check if session is valid (not expired and within timeout period)
     */
    fun isSessionValid(): Boolean {
        val lastAuthTime = securePreferences.getLong(SecurePreferences.KEY_LAST_AUTH_TIME, 0L)
        val currentTime = System.currentTimeMillis()
        
        // Session timeout: 30 minutes of inactivity
        val sessionTimeout = 30 * 60 * 1000
        
        if (currentTime - lastAuthTime > sessionTimeout) {
            return false
        }
        
        val token = getAccessToken() ?: return false
        return !isTokenExpired(token)
    }
    
    /**
     * Update last activity time (for session management)
     */
    fun updateActivity() {
        securePreferences.putLong(SecurePreferences.KEY_LAST_AUTH_TIME, System.currentTimeMillis())
    }
    
    /**
     * Validate token signature (basic validation)
     */
    fun validateTokenFormat(token: String): Boolean {
        val parts = token.split(".")
        if (parts.size != 3) return false
        
        return try {
            // Try to decode each part
            Base64.decode(parts[0], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            Base64.decode(parts[2], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * JWT Claims data class
     */
    data class JwtClaims(
        val subject: String,
        val issuer: String,
        val audience: String,
        val expiration: Long,
        val issuedAt: Long,
        val userId: String,
        val email: String,
        val roles: List<String>
    ) {
        val isExpired: Boolean
            get() = Date().time >= expiration * 1000
    }
}


