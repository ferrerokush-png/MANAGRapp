package com.managr.app.personal

import android.app.Application
import android.util.Log
import com.managr.app.core.data.notification.NotificationChannels
import com.managr.app.core.data.notification.NotificationScheduler
import com.managr.app.personal.error.GlobalExceptionHandler
import com.managr.app.personal.security.SecurityManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MANAGRApp : Application() {

    @Inject
    lateinit var globalExceptionHandler: GlobalExceptionHandler

    @Inject
    lateinit var securityManager: SecurityManager

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        // Initialize security system first (critical for app protection)
        initializeSecurity()

        // Initialize global exception handler
        globalExceptionHandler.initialize()

        // Initialize notification channels
        NotificationChannels.createChannels(this)

        // Schedule daily deadline reminders
        val notificationScheduler = NotificationScheduler(this)
        notificationScheduler.scheduleDeadlineReminders()
    }

    private fun initializeSecurity() {
        applicationScope.launch {
            try {
                val result = securityManager.initialize()

                when (result) {
                    is SecurityManager.SecurityInitResult.Success -> {
                        Log.i("MANAGR", "Security initialized successfully")
                        if (result.warnings.isNotEmpty()) {
                            Log.w("MANAGR", "Security warnings: ${result.warnings.size} threats detected")
                        }
                    }
                    is SecurityManager.SecurityInitResult.Failed -> {
                        Log.e("MANAGR", "Security initialization failed: ${result.reason}")
                        // In production, you might want to block app usage here
                        // For now, we'll just log the error
                    }
                }
            } catch (e: Exception) {
                Log.e("MANAGR", "Security initialization error", e)
            }
        }
    }
}

