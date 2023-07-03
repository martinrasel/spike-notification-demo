package de.bembelnaut.spike.notificationdemo.nofication

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import de.bembelnaut.spike.notificationdemo.alarm.AlarmItem
import de.bembelnaut.spike.notificationdemo.alarm.AndroidAlarmScheduler
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class RemindMeNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        intent?.run {
            Log.i("TEST", "onReceive: action: $action")

            val taskId = intent.getStringExtra("TASK_ID") ?: "n/a"
            val title = intent.getStringExtra("TITLE") ?: "n/a"
            val message = intent.getStringExtra("MESSAGE") ?: "n/a"
            Log.i("TEST", "onReceive: task id: $taskId")
            Log.i("TEST", "onReceive: title: $title")
            Log.i("TEST", "onReceive: message: $message")

            val alarmScheduler = AndroidAlarmScheduler(context)

            when(action) {
                ACTION_REMIND_ME_LATER -> {
                    // button actions won't be removed after on click; remove it manually
                    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(taskId.hashCode())

                    alarmScheduler.schedule(
                        AlarmItem(
                            LocalDateTime.now(ZoneId.systemDefault()).plusSeconds(10L),
                            title,
                            message,
                            UUID.fromString(taskId)
                        )
                    )
                }
                ACTION_DELETE -> {
                    alarmScheduler.cancel(UUID.fromString(taskId))
                }
            }
        }
    }

    companion object {
        const val ACTION_REMIND_ME_LATER = "remind_me_later"
        const val ACTION_DELETE = "not_today"
    }
}