package com.example.releaseflow.core.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.releaseflow.core.domain.util.Constants

/**
 * Notification channels for Release Flow
 */
object NotificationChannels {
    
    /**
     * Create all notification channels
     */
    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Releases channel (high priority)
            val releasesChannel = NotificationChannel(
                Constants.Notification.CHANNEL_ID_RELEASES,
                "Release Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for upcoming releases and upload deadlines"
                enableVibration(true)
                enableLights(true)
            }
            
            // Tasks channel (default priority)
            val tasksChannel = NotificationChannel(
                Constants.Notification.CHANNEL_ID_TASKS,
                "Task Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for task deadlines"
                enableVibration(true)
            }
            
            // Insights channel (low priority)
            val insightsChannel = NotificationChannel(
                Constants.Notification.CHANNEL_ID_INSIGHTS,
                "Insights & Tips",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "AI-powered insights and recommendations"
            }
            
            notificationManager.createNotificationChannels(
                listOf(releasesChannel, tasksChannel, insightsChannel)
            )
        }
    }
    
    /**
     * Get notification importance for channel
     */
    fun getChannelImportance(channelId: String): Int {
        return when (channelId) {
            Constants.Notification.CHANNEL_ID_RELEASES -> NotificationCompat.PRIORITY_HIGH
            Constants.Notification.CHANNEL_ID_TASKS -> NotificationCompat.PRIORITY_DEFAULT
            Constants.Notification.CHANNEL_ID_INSIGHTS -> NotificationCompat.PRIORITY_LOW
            else -> NotificationCompat.PRIORITY_DEFAULT
        }
    }
}
