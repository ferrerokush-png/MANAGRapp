package com.managr.app.core.data.security

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Base64

/**
 * OAuth 2.0 with PKCE (Proof Key for Code Exchange) Manager
 * Implements secure OAuth 2.0 authorization flow
 */
@Singleton
class OAuth2Manager @Inject constructor(
    private val securePreferences: SecurePreferences,
    private val jwtTokenManager: JwtTokenManager
) {
    
    private val secureRandom = SecureRandom()
    
    /**
     * Generate PKCE code verifier (RFC 7636)
     */
    fun generateCodeVerifier(): String {
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }
    
    /**
     * Generate PKCE code challenge from verifier
     */
    fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(Charsets.US_ASCII)
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(bytes)
        return Base64.encodeToString(digest, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }
    
    /**
     * Generate OAuth 2.0 authorization URL with PKCE
     */
    fun generateAuthorizationUrl(
        authorizationEndpoint: String,
        clientId: String,
        redirectUri: String,
        scope: String,
        state: String? = null
    ): AuthorizationRequest {
        val codeVerifier = generateCodeVerifier()
        val codeChallenge = generateCodeChallenge(codeVerifier)
        val stateValue = state ?: generateState()
        
        // Save state and code verifier for validation
        securePreferences.putString(KEY_CODE_VERIFIER, codeVerifier)
        securePreferences.putString(KEY_STATE, stateValue)
        
        val uri = Uri.parse(authorizationEndpoint).buildUpon()
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("redirect_uri", redirectUri)
            .appendQueryParameter("scope", scope)
            .appendQueryParameter("state", stateValue)
            .appendQueryParameter("code_challenge", codeChallenge)
            .appendQueryParameter("code_challenge_method", "S256")
            .build()
        
        return AuthorizationRequest(
            url = uri.toString(),
            state = stateValue,
            codeVerifier = codeVerifier
        )
    }
    
    /**
     * Validate authorization response
     */
    fun validateAuthorizationResponse(
        redirectUri: String,
        expectedState: String? = null
    ): AuthorizationResponse {
        val uri = Uri.parse(redirectUri)
        val state = uri.getQueryParameter("state")
        val code = uri.getQueryParameter("code")
        val error = uri.getQueryParameter("error")
        
        // Validate state (CSRF protection)
        val savedState = expectedState ?: securePreferences.getString(KEY_STATE) ?: ""
        if (state != savedState) {
            throw SecurityException("Invalid state parameter - possible CSRF attack")
        }
        
        if (error != null) {
            val errorDescription = uri.getQueryParameter("error_description") ?: "Unknown error"
            throw OAuth2Exception(error, errorDescription)
        }
        
        if (code.isNullOrBlank()) {
            throw IllegalStateException("No authorization code received")
        }
        
        return AuthorizationResponse(
            code = code,
            state = state
        )
    }
    
    /**
     * Exchange authorization code for access token
     */
    suspend fun exchangeCodeForToken(
        tokenEndpoint: String,
        clientId: String,
        code: String,
        redirectUri: String,
        codeVerifier: String? = null
    ): TokenResponse = withContext(Dispatchers.IO) {
        val verifier = codeVerifier ?: securePreferences.getString(KEY_CODE_VERIFIER)
            ?: throw IllegalStateException("Code verifier not found")
        
        // Make token exchange request
        // This should be implemented with your HTTP client (Retrofit)
        // For now, returning a placeholder
        
        TokenResponse(
            accessToken = "",
            refreshToken = null,
            expiresIn = 3600,
            tokenType = "Bearer"
        )
    }
    
    /**
     * Refresh access token
     */
    suspend fun refreshAccessToken(
        tokenEndpoint: String,
        clientId: String,
        refreshToken: String
    ): TokenResponse = withContext(Dispatchers.IO) {
        // Make token refresh request
        // This should be implemented with your HTTP client (Retrofit)
        
        TokenResponse(
            accessToken = "",
            refreshToken = null,
            expiresIn = 3600,
            tokenType = "Bearer"
        )
    }
    
    /**
     * Revoke token
     */
    suspend fun revokeToken(
        revocationEndpoint: String,
        token: String,
        tokenTypeHint: String = "access_token"
    ) = withContext(Dispatchers.IO) {
        // Make token revocation request
        // This should be implemented with your HTTP client (Retrofit)
    }
    
    /**
     * Generate random state for CSRF protection
     */
    private fun generateState(): String {
        val bytes = ByteArray(16)
        secureRandom.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }
    
    /**
     * Clear OAuth session data
     */
    fun clearSession() {
        securePreferences.remove(KEY_CODE_VERIFIER)
        securePreferences.remove(KEY_STATE)
        jwtTokenManager.clearTokens()
    }
    
    data class AuthorizationRequest(
        val url: String,
        val state: String,
        val codeVerifier: String
    )
    
    data class AuthorizationResponse(
        val code: String,
        val state: String
    )
    
    data class TokenResponse(
        val accessToken: String,
        val refreshToken: String?,
        val expiresIn: Long,
        val tokenType: String
    )
    
    class OAuth2Exception(
        val error: String,
        val errorDescription: String
    ) : Exception("OAuth2 Error: $error - $errorDescription")
    
    companion object {
        private const val KEY_CODE_VERIFIER = "oauth2_code_verifier"
        private const val KEY_STATE = "oauth2_state"
    }
}

