package de.bembelnaut.spike.notificationdemo.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import de.bembelnaut.spike.notificationdemo.nofication.RemindMeNotificationService


// HILT ENTRY POINT
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("TEST", "onReceive: alarm! ${intent.action}")

        val title = intent.getStringExtra("TITLE") ?: "No title..."
        val message = intent.getStringExtra("MESSAGE") ?: "No message..."
        val taskId = intent.getStringExtra("TASK_ID") ?: "No task id..."

        Log.i("TEST", "onReceive: title $title")
        Log.i("TEST", "onReceive: message $message")
        Log.i("TEST", "onReceive: task id $taskId")

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        val service = RemindMeNotificationService(context)

        service.showNotification(title, message, taskId)

    }

    companion object {
        const val ACTION_ALARM = "alarm"
    }
}