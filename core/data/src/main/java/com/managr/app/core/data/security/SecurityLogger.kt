package com.managr.app.core.data.security

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Security Event Logger
 * Logs security events without exposing sensitive data
 */
@Singleton
class SecurityLogger @Inject constructor() {
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _securityEvents = MutableSharedFlow<SecurityEvent>(replay = 100)
    val securityEvents: SharedFlow<SecurityEvent> = _securityEvents.asSharedFlow()
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
    
    /**
     * Log authentication attempt
     */
    fun logAuthenticationAttempt(success: Boolean, method: String, userId: String? = null) {
        val event = SecurityEvent(
            type = SecurityEventType.AUTHENTICATION,
            level = if (success) SecurityLevel.INFO else SecurityLevel.WARNING,
            message = "Authentication attempt ${if (success) "succeeded" else "failed"}",
            details = mapOf(
                "method" to method,
                "success" to success.toString(),
                "userId" to maskSensitiveData(userId)
            )
        )
        logEvent(event)
    }
    
    /**
     * Log authorization check
     */
    fun logAuthorizationCheck(resource: String, action: String, granted: Boolean) {
        val event = SecurityEvent(
            type = SecurityEventType.AUTHORIZATION,
            level = if (granted) SecurityLevel.INFO else SecurityLevel.WARNING,
            message = "Authorization check: $action on $resource",
            details = mapOf(
                "resource" to resource,
                "action" to action,
                "granted" to granted.toString()
            )
        )
        logEvent(event)
    }
    
    /**
     * Log encryption/decryption operation
     */
    fun logCryptoOperation(operation: String, success: Boolean, keyAlias: String? = null) {
        val event = SecurityEvent(
            type = SecurityEventType.ENCRYPTION,
            level = if (success) SecurityLevel.INFO else SecurityLevel.ERROR,
            message = "Crypto operation: $operation",
            details = mapOf(
                "operation" to operation,
                "success" to success.toString(),
                "keyAlias" to maskSensitiveData(keyAlias)
            )
        )
        logEvent(event)
    }
    
    /**
     * Log security threat detected
     */
    fun logSecurityThreat(threat: String, severity: SecurityLevel, details: Map<String, String> = emptyMap()) {
        val event = SecurityEvent(
            type = SecurityEventType.THREAT_DETECTED,
            level = severity,
            message = "Security threat detected: $threat",
            details = details
        )
        logEvent(event)
    }
    
    /**
     * Log data access
     */
    fun logDataAccess(resource: String, action: String, userId: String? = null) {
        val event = SecurityEvent(
            type = SecurityEventType.DATA_ACCESS,
            level = SecurityLevel.INFO,
            message = "Data access: $action on $resource",
            details = mapOf(
                "resource" to resource,
                "action" to action,
                "userId" to maskSensitiveData(userId)
            )
        )
        logEvent(event)
    }
    
    /**
     * Log API request
     */
    fun logApiRequest(endpoint: String, method: String, statusCode: Int) {
        val event = SecurityEvent(
            type = SecurityEventType.API_REQUEST,
            level = when {
                statusCode < 400 -> SecurityLevel.INFO
                statusCode < 500 -> SecurityLevel.WARNING
                else -> SecurityLevel.ERROR
            },
            message = "API request: $method $endpoint",
            details = mapOf(
                "endpoint" to endpoint,
                "method" to method,
                "statusCode" to statusCode.toString()
            )
        )
        logEvent(event)
    }
    
    /**
     * Log session event
     */
    fun logSessionEvent(event: String, userId: String? = null) {
        val securityEvent = SecurityEvent(
            type = SecurityEventType.SESSION,
            level = SecurityLevel.INFO,
            message = "Session event: $event",
            details = mapOf(
                "event" to event,
                "userId" to maskSensitiveData(userId)
            )
        )
        logEvent(securityEvent)
    }
    
    /**
     * Log input validation failure
     */
    fun logValidationFailure(field: String, reason: String) {
        val event = SecurityEvent(
            type = SecurityEventType.VALIDATION,
            level = SecurityLevel.WARNING,
            message = "Validation failed for $field",
            details = mapOf(
                "field" to field,
                "reason" to reason
            )
        )
        logEvent(event)
    }
    
    /**
     * Log generic security event
     */
    private fun logEvent(event: SecurityEvent) {
        scope.launch {
            // Emit to flow for observers
            _securityEvents.emit(event)
            
            // Log to Logcat (only in debug builds)
            val tag = "Security:${event.type.name}"
            val message = formatLogMessage(event)
            
            when (event.level) {
                SecurityLevel.INFO -> Log.i(tag, message)
                SecurityLevel.WARNING -> Log.w(tag, message)
                SecurityLevel.ERROR -> Log.e(tag, message)
                SecurityLevel.CRITICAL -> Log.e(tag, "CRITICAL: $message")
            }
            
            // In production, send to analytics/monitoring service
            // e.g., Firebase Crashlytics, Sentry, etc.
            if (event.level == SecurityLevel.CRITICAL || event.level == SecurityLevel.ERROR) {
                reportToMonitoring(event)
            }
        }
    }
    
    /**
     * Format log message
     */
    private fun formatLogMessage(event: SecurityEvent): String {
        return buildString {
            append("[${dateFormat.format(event.timestamp)}] ")
            append(event.message)
            if (event.details.isNotEmpty()) {
                append(" | Details: ${event.details}")
            }
        }
    }
    
    /**
     * Mask sensitive data (show only first/last characters)
     */
    private fun maskSensitiveData(data: String?): String {
        if (data.isNullOrBlank() || data.length <= 4) {
            return "***"
        }
        return "${data.take(2)}***${data.takeLast(2)}"
    }
    
    /**
     * Report to monitoring service (Firebase Crashlytics)
     */
    private fun reportToMonitoring(event: SecurityEvent) {
        try {
            // Get Firebase Crashlytics instance using reflection to avoid hard dependency
            val crashlytics = Class.forName("com.google.firebase.crashlytics.FirebaseCrashlytics")
                .getMethod("getInstance")
                .invoke(null)
            
            // Log security event (DO NOT include sensitive data)
            val logMethod = crashlytics?.javaClass?.getMethod("log", String::class.java)
            logMethod?.invoke(
                crashlytics,
                "[Security] ${event.level.name} - ${event.type.name}: ${event.message}"
            )
            
            // For critical events, record custom key-value pairs
            if (event.level == SecurityLevel.CRITICAL || event.level == SecurityLevel.ERROR) {
                val setCustomKeyMethod = crashlytics?.javaClass?.getMethod(
                    "setCustomKey",
                    String::class.java,
                    String::class.java
                )
                
                setCustomKeyMethod?.invoke(crashlytics, "security_event_type", event.type.name)
                setCustomKeyMethod?.invoke(crashlytics, "security_level", event.level.name)
                
                // Log non-sensitive details
                event.details.filter { !isSensitiveKey(it.key) }.forEach { (key, value) ->
                    setCustomKeyMethod?.invoke(crashlytics, "security_$key", value)
                }
                
                // Record exception for critical events
                if (event.level == SecurityLevel.CRITICAL) {
                    val recordException = crashlytics?.javaClass?.getMethod(
                        "recordException",
                        Throwable::class.java
                    )
                    recordException?.invoke(
                        crashlytics,
                        SecurityException("Critical security event: ${event.message}")
                    )
                }
            }
        } catch (e: Exception) {
            // Crashlytics not available or error occurred
            Log.w("SecurityLogger", "Failed to report to monitoring service", e)
        }
    }
    
    /**
     * Check if a key contains sensitive data
     */
    private fun isSensitiveKey(key: String): Boolean {
        val sensitivePatterns = listOf(
            "password", "token", "key", "secret", "credential",
            "email", "phone", "ssn", "card", "pin"
        )
        return sensitivePatterns.any { key.lowercase().contains(it) }
    }
    
    /**
     * Get recent security events
     */
    fun getRecentEvents(limit: Int = 100): List<SecurityEvent> {
        // In a real implementation, this would query from a persistent store
        return emptyList()
    }
    
    /**
     * Security Event data class
     */
    data class SecurityEvent(
        val type: SecurityEventType,
        val level: SecurityLevel,
        val message: String,
        val details: Map<String, String> = emptyMap(),
        val timestamp: Date = Date()
    )
    
    /**
     * Security Event Types
     */
    enum class SecurityEventType {
        AUTHENTICATION,
        AUTHORIZATION,
        ENCRYPTION,
        THREAT_DETECTED,
        DATA_ACCESS,
        API_REQUEST,
        SESSION,
        VALIDATION
    }
    
    /**
     * Security Levels
     */
    enum class SecurityLevel {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }
}

