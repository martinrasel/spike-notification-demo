package de.bembelnaut.spike.notificationdemo

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import java.time.LocalDate
import java.util.UUID

class RemindMeNotificationService(
    private val context: Context
) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(
        taskName: String,
        taskId: UUID,
        dateTime: LocalDate,
        message: String
    ) {
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("ACTION", "CLICK")
            putExtra("TASK_NAME", taskName)
            putExtra("TASK_ID", taskId.toString())
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP // otherwise onNewIntent in activity isn't called; instead onCreate
        }

        val activityPendingIntent = PendingIntent.getActivity(
            context,
            taskId.hashCode(),
            activityIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val buttonPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, RemindMeNotificationReceiver::class.java).apply {
                putExtra("ACTION", "NOT_TODAY")
                putExtra("TASK_NAME", taskName)
                putExtra("TASK_ID", taskId.toString())
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // update current required to send extra data
        )

        val deletePendingIntent = PendingIntent.getBroadcast(
            context,
            3,
            Intent(context, RemindMeNotificationReceiver::class.java).apply {
                putExtra("ACTION", "DELETE")
                putExtra("TASK_NAME", taskName)
                putExtra("TASK_ID", taskId.toString())
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // update current required to send extra data
        )

        val notification = NotificationCompat.Builder(context, REMIND_ME_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("Reminder")
            .setContentText(message)
            .setContentIntent(activityPendingIntent)
            .setOngoing(false) // can't cancel your notification - user must do this explicit; except notificationManager.cancel(); doesnt work with auto cancel and button action
            //.setLights(Color.Blue.toArgb(), 500, 500) // works only with LED...
            .setDeleteIntent(deletePendingIntent) // when user clear notification
            .setAutoCancel(true) // clicking the notification clears it
            //.setStyle(...) // see https://developer.android.com/develop/ui/views/notifications/expanded
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL) // a "dot" on the app icon
            .setNumber(3) // counter on app icon
            //.setBubbleMetadata() // a "bubble" beside the screen - like FB new message
            .addAction(
                R.drawable.ic_notification_icon,
                "Not today...",
                buttonPendingIntent
            )
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val REMIND_ME_NOTIFICATION_CHANNEL_ID = "de.bembelnaut.spike.notificationdemo.channel.remindme"
    }
}