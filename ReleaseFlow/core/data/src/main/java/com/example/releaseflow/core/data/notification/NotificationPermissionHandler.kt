package com.example.releaseflow.core.data.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Handler for notification permissions (Android 13+)
 */
class NotificationPermissionHandler(private val context: Context) {
    
    /**
     * Check if notification permission is granted
     */
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // No permission needed before Android 13
        }
    }
    
    /**
     * Get required permission string
     */
    fun getRequiredPermission(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else {
            null
        }
    }
    
    /**
     * Check if should show permission rationale
     */
    fun shouldShowRationale(): Boolean {
        // This should be called from an Activity context
        return false // Placeholder
    }
}
