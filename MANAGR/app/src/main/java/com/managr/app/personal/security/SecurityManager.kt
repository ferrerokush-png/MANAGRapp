package com.managr.app.personal.security

import android.content.Context
import com.managr.app.core.data.security.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central Security Manager
 * Coordinates all security operations and provides unified security API
 */
@Singleton
class SecurityManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val runtimeSecurityManager: RuntimeSecurityManager,
    private val biometricAuthManager: BiometricAuthManager,
    private val encryptionManager: EncryptionManager,
    private val securePreferences: SecurePreferences,
    private val inputValidator: InputValidator,
    private val jwtTokenManager: JwtTokenManager,
    private val oauth2Manager: OAuth2Manager,
    private val securityLogger: SecurityLogger
) {
    
    private val _securityState = MutableStateFlow<SecurityState>(SecurityState.Initializing)
    val securityState: StateFlow<SecurityState> = _securityState.asStateFlow()
    
    /**
     * Initialize security system
     * Performs security checks and sets up security infrastructure
     */
    suspend fun initialize(): SecurityInitResult {
        _securityState.value = SecurityState.Initializing
        
        try {
            // Perform runtime security checks
            val securityCheck = runtimeSecurityManager.performSecurityCheck()
            
            if (!securityCheck.isSecure) {
                // Log security threats
                securityCheck.threats.forEach { threat ->
                    securityLogger.logSecurityThreat(
                        threat = threat.description,
                        severity = SecurityLogger.SecurityLevel.CRITICAL
                    )
                }
                
                // Determine if app should continue despite threats
                val criticalThreats = securityCheck.threats.filter { threat ->
                    threat is RuntimeSecurityManager.SecurityThreat.AppTampered ||
                    threat is RuntimeSecurityManager.SecurityThreat.DebuggerAttached
                }
                
                if (criticalThreats.isNotEmpty()) {
                    _securityState.value = SecurityState.SecurityThreatDetected(securityCheck.threats)
                    return SecurityInitResult.Failed(
                        reason = "Critical security threats detected",
                        threats = securityCheck.threats
                    )
                }
            }
            
            // Check biometric availability
            val biometricAvailability = biometricAuthManager.isBiometricAvailable()
            
            // Initialize encryption keys if needed
            ensureEncryptionKeysInitialized()
            
            _securityState.value = SecurityState.Secure(
                biometricAvailable = biometricAvailability.isAvailable,
                threats = securityCheck.threats
            )
            
            securityLogger.logSessionEvent("Security system initialized successfully")
            
            return SecurityInitResult.Success(
                biometricAvailable = biometricAvailability.isAvailable,
                warnings = securityCheck.threats
            )
            
        } catch (e: Exception) {
            securityLogger.logSecurityThreat(
                threat = "Security initialization failed: ${e.message}",
                severity = SecurityLogger.SecurityLevel.ERROR
            )
            
            _securityState.value = SecurityState.Error(e.message ?: "Unknown error")
            
            return SecurityInitResult.Failed(
                reason = e.message ?: "Unknown error",
                threats = emptyList()
            )
        }
    }
    
    /**
     * Ensure encryption keys are initialized
     */
    private fun ensureEncryptionKeysInitialized() {
        if (!encryptionManager.keyExists(DEFAULT_KEY_ALIAS)) {
            // Key will be created automatically on first use
            securityLogger.logCryptoOperation("Master key initialization", success = true)
        }
    }
    
    /**
     * Perform periodic security check
     */
    suspend fun performPeriodicCheck(): Boolean {
        val check = runtimeSecurityManager.performSecurityCheck()
        
        if (!check.isSecure) {
            check.threats.forEach { threat ->
                securityLogger.logSecurityThreat(
                    threat = threat.description,
                    severity = SecurityLogger.SecurityLevel.WARNING
                )
            }
        }
        
        return check.isSecure
    }
    
    /**
     * Validate user input with security checks
     */
    fun validateInput(input: String, type: InputType): ValidationResult {
        return try {
            val isValid = when (type) {
                InputType.EMAIL -> inputValidator.isValidEmail(input)
                InputType.URL -> inputValidator.isValidUrl(input)
                InputType.PROJECT_TITLE -> inputValidator.isValidProjectTitle(input)
                InputType.TASK_TITLE -> inputValidator.isValidTaskTitle(input)
                InputType.DESCRIPTION -> inputValidator.isValidDescription(input)
            }
            
            if (!isValid) {
                securityLogger.logValidationFailure(type.name, "Invalid format")
            }
            
            ValidationResult(
                isValid = isValid,
                sanitizedInput = if (isValid) inputValidator.sanitizeString(input) else null
            )
        } catch (e: Exception) {
            securityLogger.logValidationFailure(type.name, e.message ?: "Unknown error")
            ValidationResult(isValid = false, sanitizedInput = null)
        }
    }
    
    /**
     * Encrypt sensitive data
     */
    fun encryptData(data: String, keyAlias: String? = null): EncryptionResult {
        return try {
            val encrypted = encryptionManager.encrypt(data, keyAlias ?: DEFAULT_KEY_ALIAS)
            securityLogger.logCryptoOperation("Data encryption", success = true)
            EncryptionResult.Success(encrypted)
        } catch (e: Exception) {
            securityLogger.logCryptoOperation("Data encryption", success = false)
            EncryptionResult.Failed(e.message ?: "Encryption failed")
        }
    }
    
    /**
     * Decrypt sensitive data
     */
    fun decryptData(encryptedData: ByteArray, keyAlias: String? = null): DecryptionResult {
        return try {
            val decrypted = encryptionManager.decrypt(encryptedData, keyAlias ?: DEFAULT_KEY_ALIAS)
            securityLogger.logCryptoOperation("Data decryption", success = true)
            DecryptionResult.Success(decrypted)
        } catch (e: Exception) {
            securityLogger.logCryptoOperation("Data decryption", success = false)
            DecryptionResult.Failed(e.message ?: "Decryption failed")
        }
    }
    
    /**
     * Check if user is authenticated
     */
    fun isAuthenticated(): Boolean {
        return jwtTokenManager.isSessionValid()
    }
    
    /**
     * Logout and clear all secure data
     */
    fun logout() {
        jwtTokenManager.clearTokens()
        oauth2Manager.clearSession()
        securityLogger.logSessionEvent("User logged out")
    }
    
    // Companion object for default key alias
    companion object {
        private const val DEFAULT_KEY_ALIAS = "managr_master_key"
    }
    
    sealed class SecurityState {
        object Initializing : SecurityState()
        data class Secure(
            val biometricAvailable: Boolean,
            val threats: List<RuntimeSecurityManager.SecurityThreat>
        ) : SecurityState()
        data class SecurityThreatDetected(
            val threats: List<RuntimeSecurityManager.SecurityThreat>
        ) : SecurityState()
        data class Error(val message: String) : SecurityState()
    }
    
    sealed class SecurityInitResult {
        data class Success(
            val biometricAvailable: Boolean,
            val warnings: List<RuntimeSecurityManager.SecurityThreat>
        ) : SecurityInitResult()
        data class Failed(
            val reason: String,
            val threats: List<RuntimeSecurityManager.SecurityThreat>
        ) : SecurityInitResult()
    }
    
    enum class InputType {
        EMAIL, URL, PROJECT_TITLE, TASK_TITLE, DESCRIPTION
    }
    
    data class ValidationResult(
        val isValid: Boolean,
        val sanitizedInput: String?
    )
    
    sealed class EncryptionResult {
        data class Success(val encrypted: ByteArray) : EncryptionResult()
        data class Failed(val error: String) : EncryptionResult()
    }
    
    sealed class DecryptionResult {
        data class Success(val decrypted: String) : DecryptionResult()
        data class Failed(val error: String) : DecryptionResult()
    }
}

