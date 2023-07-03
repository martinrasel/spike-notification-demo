package de.bembelnaut.spike.notificationdemo.alarm

import java.util.UUID

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(taskId: UUID)
}