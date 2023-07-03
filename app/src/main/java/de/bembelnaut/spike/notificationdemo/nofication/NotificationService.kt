package de.bembelnaut.spike.notificationdemo.nofication

interface NotificationService {
    fun showNotification(item: NotificationItem)
    fun removeNotification(item: NotificationItem)
}