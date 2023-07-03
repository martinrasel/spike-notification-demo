package de.bembelnaut.spike.notificationdemo.nofication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RemindMeNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.run {
            Log.i("TEST", "onReceive: action: $action")

            val taskId = intent.getStringExtra("TASK_ID") ?: "n/a"
            Log.i("TEST", "onReceive: task id: $taskId")
        }
    }

    companion object {
        const val ACTION_REMIND_ME_LATER = "remind_me_later"
        const val ACTION_DELETE = "not_today"
    }
}