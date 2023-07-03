package de.bembelnaut.spike.notificationdemo.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import de.bembelnaut.spike.notificationdemo.alarm.AlarmReceiver.Companion.ACTION_ALARM
import java.time.ZoneId
import java.util.UUID

class AndroidAlarmScheduler(
    private val context: Context,
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {

        val broadcastIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_ALARM
            putExtra("TITLE", item.title)
            putExtra("MESSAGE", item.message)
            putExtra("TASK_ID", item.taskId.toString())
        }

        val pendingBroadcastIntent = PendingIntent.getBroadcast(
            context,
            item.taskId.toString().hashCode(),
            broadcastIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            pendingBroadcastIntent,
        )
    }

    override fun cancel(taskId: UUID) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                taskId.toString().hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }
}