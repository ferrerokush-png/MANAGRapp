package com.managr.app.core.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class MarkTaskCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(NotificationHelper.EXTRA_TASK_ID, -1L)
        if (taskId > 0) {
            Log.i("MarkTaskCompleteReceiver", "Task $taskId marked complete via notification action")
            // Cancel the related notification
            NotificationManagerCompat.from(context)
                .cancel((taskId + 20000).toInt())
            // In a full implementation, update repository state here.
        } else {
            Log.w("MarkTaskCompleteReceiver", "Missing or invalid taskId in intent")
        }
    }
}

