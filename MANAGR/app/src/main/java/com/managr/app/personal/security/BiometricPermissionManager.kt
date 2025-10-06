package com.managr.app.personal.security

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Biometric Permission Manager
 * Handles runtime permissions for biometric authentication on Android 13+
 */
@Singleton
class BiometricPermissionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    /**
     * Check if biometric permission is granted
     * On Android 13+ (API 33), USE_BIOMETRIC requires runtime permission
     */
    fun hasBiometricPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ requires USE_BIOMETRIC permission
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_BIOMETRIC
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Pre-Android 13 doesn't require runtime permission for biometrics
            true
        }
    }
    
    /**
     * Check if biometric hardware is available and enrolled
     */
    fun isBiometricEnrolled(biometricAuthManager: BiometricAuthManager): Boolean {
        return biometricAuthManager.isBiometricAvailable().isAvailable
    }
    
    /**
     * Get required biometric permissions for current Android version
     */
    fun getRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.USE_BIOMETRIC)
        } else {
            emptyArray()
        }
    }
    
    /**
     * Create permission launcher for requesting biometric permission
     * Must be called during activity initialization (before onCreate returns)
     */
    fun createPermissionLauncher(
        activity: ComponentActivity,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ): ActivityResultLauncher<Array<String>> {
        return activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                onGranted()
            } else {
                onDenied()
            }
        }
    }
    
    /**
     * Request biometric permission
     */
    fun requestBiometricPermission(launcher: ActivityResultLauncher<Array<String>>) {
        val permissions = getRequiredPermissions()
        if (permissions.isNotEmpty()) {
            launcher.launch(permissions)
        }
    }
    
    /**
     * Check if we should show permission rationale
     */
    fun shouldShowPermissionRationale(activity: ComponentActivity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.shouldShowRequestPermissionRationale(Manifest.permission.USE_BIOMETRIC)
        } else {
            false
        }
    }
}

/**
 * Biometric Permission State
 */
sealed class BiometricPermissionState {
    object Granted : BiometricPermissionState()
    object Denied : BiometricPermissionState()
    object NotRequired : BiometricPermissionState()
    object RationaleNeeded : BiometricPermissionState()
}


