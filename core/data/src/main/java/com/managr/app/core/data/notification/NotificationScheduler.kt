package com.managr.app.core.data.notification

import android.content.Context
import androidx.work.*
import com.managr.app.core.data.worker.DeadlineReminderWorker
import java.util.concurrent.TimeUnit

/**
 * Scheduler for notification workers
 * Sets up periodic work to check deadlines daily
 */
class NotificationScheduler(private val context: Context) {
    
    private val workManager = WorkManager.getInstance(context)
    
    companion object {
        private const val DEADLINE_REMINDER_WORK_NAME = "deadline_reminder_work"
    }
    
    /**
     * Schedule daily deadline checks
     * Runs once per day at 9 AM
     */
    fun scheduleDeadlineReminders() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()
        
        val dailyWorkRequest = PeriodicWorkRequestBuilder<DeadlineReminderWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .addTag("notifications")
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            DEADLINE_REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )
    }
    
    /**
     * Schedule immediate deadline check
     */
    fun scheduleImmediateCheck() {
        val immediateWorkRequest = OneTimeWorkRequestBuilder<DeadlineReminderWorker>()
            .addTag("notifications")
            .build()
        
        workManager.enqueue(immediateWorkRequest)
    }
    
    /**
     * Cancel all scheduled notifications
     */
    fun cancelAllNotifications() {
        workManager.cancelUniqueWork(DEADLINE_REMINDER_WORK_NAME)
        workManager.cancelAllWorkByTag("notifications")
    }
    
    /**
     * Calculate initial delay to run at 9 AM
     */
    private fun calculateInitialDelay(): Long {
        val now = java.time.LocalDateTime.now()
        val targetTime = now.withHour(9).withMinute(0).withSecond(0)
        
        val target = if (now.isAfter(targetTime)) {
            targetTime.plusDays(1)
        } else {
            targetTime
        }
        
        return java.time.Duration.between(now, target).toMillis()
    }
    
    /**
     * Check if notifications are scheduled
     */
    fun areNotificationsScheduled(): Boolean {
        val workInfos = workManager.getWorkInfosForUniqueWork(DEADLINE_REMINDER_WORK_NAME).get()
        return workInfos.any { it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING }
    }
}
