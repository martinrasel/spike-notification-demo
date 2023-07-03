package de.bembelnaut.spike.notificationdemo.nofication

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import de.bembelnaut.spike.notificationdemo.MainActivity
import de.bembelnaut.spike.notificationdemo.MainActivity.Companion.START_TASK
import de.bembelnaut.spike.notificationdemo.R
import de.bembelnaut.spike.notificationdemo.nofication.RemindMeNotificationReceiver.Companion.ACTION_DELETE
import de.bembelnaut.spike.notificationdemo.nofication.RemindMeNotificationReceiver.Companion.ACTION_REMIND_ME_LATER

class RemindMeNotificationService(
    private val context: Context
): NotificationService {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showNotification(
        item: NotificationItem
    ) {
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            action = START_TASK
            putExtra("TASK_ID", item.taskId)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP // otherwise onNewIntent in activity isn't called; instead onCreate
        }

        val activityPendingIntent = PendingIntent.getActivity(
            context,
            item.taskId.hashCode(),
            activityIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val buttonPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_REMIND_LATER,
            Intent(context, RemindMeNotificationReceiver::class.java).apply {
                action = ACTION_REMIND_ME_LATER
                putExtra("TASK_ID", item.taskId)
                putExtra("TITLE", item.title)
                putExtra("MESSAGE", item.message)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // update current required to send extra data
        )

        val deletePendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DELETE_TASK,
            Intent(context, RemindMeNotificationReceiver::class.java).apply {
                action = ACTION_DELETE
                putExtra("TASK_ID", item.taskId)
                putExtra("TITLE", item.title)
                putExtra("MESSAGE", item.message)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // update current required to send extra data
        )

        val notification = NotificationCompat.Builder(context, REMIND_ME_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(item.title)
            .setContentText(item.message)
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
                "Remind me later...",
                buttonPendingIntent
            )
            .build()

        val notificationId = item.taskId.hashCode()

        notificationManager.notify(notificationId, notification)
    }

    override fun removeNotification(item: NotificationItem) {
        // button actions won't be removed after on click; remove it manually
        notificationManager.cancel(item.taskId.hashCode())
    }

    companion object {
        const val REMIND_ME_NOTIFICATION_CHANNEL_ID = "de.bembelnaut.spike.notificationdemo.channel.remindme"
        const val REQUEST_CODE_REMIND_LATER = 1
        const val REQUEST_CODE_DELETE_TASK = 2
    }
}