package com.example.releaseflow.core.data.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.releaseflow.core.data.R
import com.example.releaseflow.core.domain.util.Constants

/**
 * Helper for creating and showing notifications
 */
class NotificationHelper(private val context: Context) {
    private val notificationManager = NotificationManagerCompat.from(context)

    /**
     * Show release reminder notification
     */
    fun showReleaseReminder(
        projectId: Long,
        projectTitle: String,
        releaseDate: String,
        daysUntil: Int
    ) {
        val notification = NotificationCompat.Builder(context, Constants.Notification.CHANNEL_ID_RELEASES)
            .setSmallIcon(R.drawable.ic_stat_releaseflow)
            .setContentTitle("Release Coming Up!")
            .setContentText("$projectTitle releases in $daysUntil days ($releaseDate)")
            .setPriority(NotificationChannels.getChannelImportance(Constants.Notification.CHANNEL_ID_RELEASES))
            .setAutoCancel(true)
            .setContentIntent(createProjectDetailIntent(projectId))
            .build()

        if (canNotify()) notificationManager.notify(projectId.toInt(), notification)
    }

    /**
     * Show upload deadline reminder
     */
    fun showUploadDeadlineReminder(
        projectId: Long,
        projectTitle: String,
        uploadDeadline: String,
        daysUntil: Int
    ) {
        val notification = NotificationCompat.Builder(context, Constants.Notification.CHANNEL_ID_RELEASES)
            .setSmallIcon(R.drawable.ic_stat_releaseflow)
            .setContentTitle("Upload Deadline Approaching!")
            .setContentText("Upload $projectTitle by $uploadDeadline ($daysUntil days)")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(createProjectDetailIntent(projectId))
            .addAction(0, "Mark Uploaded", createMarkUploadedIntent(projectId))
            .build()

        if (canNotify()) notificationManager.notify((projectId + 10000).toInt(), notification)
    }

    /**
     * Show task reminder notification
     */
    fun showTaskReminder(
        taskId: Long,
        projectId: Long,
        taskTitle: String,
        dueDate: String,
        daysUntil: Int
    ) {
        val notification = NotificationCompat.Builder(context, Constants.Notification.CHANNEL_ID_TASKS)
            .setSmallIcon(R.drawable.ic_stat_releaseflow)
            .setContentTitle("Task Due Soon")
            .setContentText("$taskTitle is due in $daysUntil days ($dueDate)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(createProjectDetailIntent(projectId))
            .addAction(0, "Mark Complete", createMarkTaskCompleteIntent(taskId))
            .build()

        if (canNotify()) notificationManager.notify((taskId + 20000).toInt(), notification)
    }

    /**
     * Show task overdue notification
     */
    fun showTaskOverdueNotification(
        taskId: Long,
        projectId: Long,
        taskTitle: String
    ) {
        val notification = NotificationCompat.Builder(context, Constants.Notification.CHANNEL_ID_TASKS)
            .setSmallIcon(R.drawable.ic_stat_releaseflow)
            .setContentTitle("Task Overdue!")
            .setContentText("$taskTitle is overdue")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(createProjectDetailIntent(projectId))
            .build()

        if (canNotify()) notificationManager.notify((taskId + 30000).toInt(), notification)
    }

    /**
     * Create intent to open project detail or fallback to app launch
     */
    private fun createProjectDetailIntent(projectId: Long): PendingIntent {
        val target: Intent = try {
            // Try to open MainActivity explicitly without compile-time dependency on :app
            val cls = Class.forName("com.example.releaseflow.personal.MainActivity")
            Intent(context, cls)
        } catch (e: ClassNotFoundException) {
            Log.w("NotificationHelper", "MainActivity not found, using launcher", e)
            val pm: PackageManager = context.packageManager
            val launch: Intent? = pm.getLaunchIntentForPackage(context.packageName)
            if (launch != null) launch else Intent(Intent.ACTION_MAIN).also { intent ->
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.setPackage(context.packageName)
            }
        }
        target.putExtra("projectId", projectId)
        target.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        return PendingIntent.getActivity(
            context,
            projectId.toInt(),
            target,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * Create intent to mark uploaded
     */
    private fun createMarkUploadedIntent(projectId: Long): PendingIntent {
        val intent = Intent(ACTION_MARK_UPLOADED)
        intent.setClassName(context, "com.example.releaseflow.core.data.notification.MarkUploadedReceiver")
        intent.putExtra(EXTRA_PROJECT_ID, projectId)

        return PendingIntent.getBroadcast(
            context,
            projectId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * Create intent to mark task complete
     */
    private fun createMarkTaskCompleteIntent(taskId: Long): PendingIntent {
        val intent = Intent(ACTION_MARK_TASK_COMPLETE)
        intent.setClassName(context, "com.example.releaseflow.core.data.notification.MarkTaskCompleteReceiver")
        intent.putExtra(EXTRA_TASK_ID, taskId)

        return PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * Cancel notification
     */
    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    /**
     * Cancel all notifications
     */
    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    private fun canNotify(): Boolean {
        val enabled = notificationManager.areNotificationsEnabled()
        if (!enabled) return false
        return if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    companion object {
        const val ACTION_MARK_UPLOADED = "com.example.releaseflow.MARK_UPLOADED"
        const val ACTION_MARK_TASK_COMPLETE = "com.example.releaseflow.MARK_TASK_COMPLETE"
        const val EXTRA_PROJECT_ID = "projectId"
        const val EXTRA_TASK_ID = "taskId"
    }
}
