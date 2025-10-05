package com.example.releaseflow.core.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class MarkUploadedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val projectId = intent.getLongExtra(NotificationHelper.EXTRA_PROJECT_ID, -1L)
        if (projectId > 0) {
            Log.i("MarkUploadedReceiver", "Project $projectId marked as uploaded via notification action")
            // Cancel the related notification
            NotificationManagerCompat.from(context)
                .cancel((projectId + 10000).toInt())
            // In a full implementation, update repository state here.
        } else {
            Log.w("MarkUploadedReceiver", "Missing or invalid projectId in intent")
        }
    }
}

