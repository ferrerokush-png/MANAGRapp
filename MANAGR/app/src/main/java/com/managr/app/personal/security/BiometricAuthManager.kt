package com.managr.app.personal.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Biometric Authentication Manager
 * Handles fingerprint, face, and device credential authentication
 */
@Singleton
class BiometricAuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val biometricManager = BiometricManager.from(context)
    
    /**
     * Check if biometric authentication is available
     */
    fun isBiometricAvailable(): BiometricAvailability {
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                BiometricAvailability.Available
            
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                BiometricAvailability.NoHardware
            
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                BiometricAvailability.HardwareUnavailable
            
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                BiometricAvailability.NoneEnrolled
            
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED ->
                BiometricAvailability.SecurityUpdateRequired
            
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED ->
                BiometricAvailability.Unsupported
            
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN ->
                BiometricAvailability.Unknown
            
            else -> BiometricAvailability.Unknown
        }
    }
    
    /**
     * Authenticate using biometrics
     */
    fun authenticate(
        activity: FragmentActivity,
        title: String = "Authentication Required",
        subtitle: String = "Verify your identity to continue",
        negativeButtonText: String = "Cancel",
        onSuccess: () -> Unit,
        onError: (errorCode: Int, errorMessage: String) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()
        
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString.toString())
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            }
        )
        
        biometricPrompt.authenticate(promptInfo)
    }
    
    /**
     * Authenticate with crypto object (for encryption/decryption)
     */
    fun authenticateWithCrypto(
        activity: FragmentActivity,
        cryptoObject: BiometricPrompt.CryptoObject,
        title: String = "Authentication Required",
        subtitle: String = "Verify your identity to continue",
        negativeButtonText: String = "Cancel",
        onSuccess: (BiometricPrompt.CryptoObject?) -> Unit,
        onError: (errorCode: Int, errorMessage: String) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeButtonText)
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
        
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result.cryptoObject)
                }
                
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString.toString())
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            }
        )
        
        biometricPrompt.authenticate(promptInfo, cryptoObject)
    }
    
    sealed class BiometricAvailability {
        object Available : BiometricAvailability()
        object NoHardware : BiometricAvailability()
        object HardwareUnavailable : BiometricAvailability()
        object NoneEnrolled : BiometricAvailability()
        object SecurityUpdateRequired : BiometricAvailability()
        object Unsupported : BiometricAvailability()
        object Unknown : BiometricAvailability()
        
        val isAvailable: Boolean
            get() = this is Available
    }
}


