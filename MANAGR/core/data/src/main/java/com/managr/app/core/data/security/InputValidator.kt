package com.managr.app.core.data.security

import android.webkit.URLUtil
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Input Validation & Sanitization
 * Prevents SQL injection, XSS, and other injection attacks
 */
@Singleton
class InputValidator @Inject constructor() {
    
    /**
     * Sanitize string input (remove potentially dangerous characters)
     */
    fun sanitizeString(input: String): String {
        return input
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("/", "&#x2F;")
            .trim()
    }
    
    /**
     * Validate email format
     */
    fun isValidEmail(email: String): Boolean {
        if (email.length < 3 || email.length > 254) return false
        return EMAIL_PATTERN.matcher(email).matches()
    }
    
    /**
     * Validate URL format
     */
    fun isValidUrl(url: String): Boolean {
        return URLUtil.isValidUrl(url) && (url.startsWith("https://") || url.startsWith("http://"))
    }
    
    /**
     * Validate phone number
     */
    fun isValidPhone(phone: String): Boolean {
        val digitsOnly = phone.replace(Regex("[^0-9]"), "")
        return digitsOnly.length in 10..15
    }
    
    /**
     * Validate project title
     */
    fun isValidProjectTitle(title: String): Boolean {
        return title.length in 1..200 && !containsSqlInjection(title)
    }
    
    /**
     * Validate task title
     */
    fun isValidTaskTitle(title: String): Boolean {
        return title.length in 1..500 && !containsSqlInjection(title)
    }
    
    /**
     * Validate description/notes
     */
    fun isValidDescription(description: String): Boolean {
        return description.length <= 5000 && !containsSqlInjection(description)
    }
    
    /**
     * Check for SQL injection patterns
     */
    fun containsSqlInjection(input: String): Boolean {
        val sqlKeywords = arrayOf(
            "SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "CREATE", "ALTER",
            "EXEC", "EXECUTE", "UNION", "DECLARE", "--", "/*", "*/", "xp_", 
            "sp_", "0x", "\\x", "CAST", "CONVERT"
        )
        
        val upperInput = input.uppercase()
        return sqlKeywords.any { upperInput.contains(it) }
    }
    
    /**
     * Check for XSS patterns
     */
    fun containsXSS(input: String): Boolean {
        val xssPatterns = arrayOf(
            "<script", "javascript:", "onerror=", "onload=", "onclick=", 
            "onmouseover=", "<iframe", "<embed", "<object", "eval(",
            "expression(", "vbscript:", "data:text/html"
        )
        
        val lowerInput = input.lowercase()
        return xssPatterns.any { lowerInput.contains(it) }
    }
    
    /**
     * Validate file name
     */
    fun isValidFileName(fileName: String): Boolean {
        if (fileName.length > 255) return false
        if (fileName.startsWith(".")) return false
        
        // Check for path traversal
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            return false
        }
        
        return FILE_NAME_PATTERN.matcher(fileName).matches()
    }
    
    /**
     * Validate MIME type for artwork uploads
     */
    fun isValidImageMimeType(mimeType: String): Boolean {
        return mimeType in ALLOWED_IMAGE_TYPES
    }
    
    /**
     * Validate file size
     */
    fun isValidFileSize(sizeBytes: Long, maxSizeMB: Long = 10): Boolean {
        val maxBytes = maxSizeMB * 1024 * 1024
        return sizeBytes in 1..maxBytes
    }
    
    /**
     * Sanitize SQL query parameter
     */
    fun sanitizeSqlParameter(input: String): String {
        return input
            .replace("'", "''") // Escape single quotes
            .replace("\"", "\\\"") // Escape double quotes
            .replace(";", "") // Remove semicolons
            .replace("--", "") // Remove SQL comments
            .trim()
    }
    
    /**
     * Validate and sanitize search query
     */
    fun sanitizeSearchQuery(query: String): String {
        if (query.length > 500) {
            throw IllegalArgumentException("Search query too long")
        }
        
        if (containsSqlInjection(query) || containsXSS(query)) {
            throw SecurityException("Invalid search query")
        }
        
        return sanitizeString(query)
    }
    
    /**
     * Validate password strength
     */
    fun isValidPassword(password: String): PasswordValidation {
        val minLength = password.length >= 8
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        
        val score = listOf(minLength, hasUpperCase, hasLowerCase, hasDigit, hasSpecialChar)
            .count { it }
        
        return PasswordValidation(
            isValid = score >= 4,
            minLength = minLength,
            hasUpperCase = hasUpperCase,
            hasLowerCase = hasLowerCase,
            hasDigit = hasDigit,
            hasSpecialChar = hasSpecialChar,
            score = score
        )
    }
    
    /**
     * Validate API key format
     */
    fun isValidApiKey(apiKey: String): Boolean {
        // API keys should be alphanumeric with specific length
        return apiKey.length in 32..128 && API_KEY_PATTERN.matcher(apiKey).matches()
    }
    
    /**
     * Validate JWT token format
     */
    fun isValidJwtToken(token: String): Boolean {
        val parts = token.split(".")
        return parts.size == 3 && parts.all { it.isNotBlank() }
    }
    
    data class PasswordValidation(
        val isValid: Boolean,
        val minLength: Boolean,
        val hasUpperCase: Boolean,
        val hasLowerCase: Boolean,
        val hasDigit: Boolean,
        val hasSpecialChar: Boolean,
        val score: Int
    ) {
        val strength: PasswordStrength
            get() = when (score) {
                5 -> PasswordStrength.STRONG
                4 -> PasswordStrength.MEDIUM
                3 -> PasswordStrength.WEAK
                else -> PasswordStrength.VERY_WEAK
            }
    }
    
    enum class PasswordStrength {
        VERY_WEAK, WEAK, MEDIUM, STRONG
    }
    
    companion object {
        private val EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
        
        private val FILE_NAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_\\-. ]+\\.[a-zA-Z0-9]+$"
        )
        
        private val API_KEY_PATTERN = Pattern.compile(
            "^[A-Za-z0-9_\\-]+$"
        )
        
        private val ALLOWED_IMAGE_TYPES = setOf(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
        )
    }
}


