package com.managr.app.core.data.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Secure SharedPreferences using Jetpack Security
 * Provides encrypted storage for sensitive key-value pairs
 */
@Singleton
class SecurePreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }
    
    private val sharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    /**
     * Save encrypted string
     */
    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    
    /**
     * Get encrypted string
     */
    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }
    
    /**
     * Save encrypted boolean
     */
    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }
    
    /**
     * Get encrypted boolean
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
    
    /**
     * Save encrypted int
     */
    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }
    
    /**
     * Get encrypted int
     */
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }
    
    /**
     * Save encrypted long
     */
    fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }
    
    /**
     * Get encrypted long
     */
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }
    
    /**
     * Remove a key
     */
    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
    
    /**
     * Clear all encrypted preferences
     */
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
    
    /**
     * Check if key exists
     */
    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
    
    companion object {
        private const val PREFS_NAME = "managr_secure_prefs"
        
        // Common keys for secure storage
        const val KEY_AUTH_TOKEN = "auth_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
        const val KEY_USER_ID = "user_id"
        const val KEY_API_KEY = "api_key"
        const val KEY_SESSION_ID = "session_id"
        const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        const val KEY_LAST_AUTH_TIME = "last_auth_time"
    }
}


