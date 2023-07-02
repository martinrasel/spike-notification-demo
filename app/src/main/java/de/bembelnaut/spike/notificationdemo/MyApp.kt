package de.bembelnaut.spike.notificationdemo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {

        val channel = NotificationChannel(
            RemindMeNotificationService.REMIND_ME_NOTIFICATION_CHANNEL_ID,
            "Learning Reminder",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used for learning reminder"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}