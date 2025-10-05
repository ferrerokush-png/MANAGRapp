package com.example.releaseflow.personal

import android.app.Application
import com.example.releaseflow.core.data.notification.NotificationChannels
import com.example.releaseflow.core.data.notification.NotificationScheduler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ReleaseFlowApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize notification channels
        NotificationChannels.createChannels(this)
        
        // Schedule daily deadline reminders
        val notificationScheduler = NotificationScheduler(this)
        notificationScheduler.scheduleDeadlineReminders()
    }
}

