package de.bembelnaut.spike.notificationdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class RemindMeNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.getStringExtra("ACTION") ?: return
        val taskName = intent.getStringExtra("TASK_NAME") ?: return
        val taskId = intent.getStringExtra("TASK_ID") ?: return

        Log.i("TEST", "onReceive: action: $action")
        Log.i("TEST", "onReceive: task name: $taskName")
        Log.i("TEST", "onReceive: task id: $taskId")
    }

}