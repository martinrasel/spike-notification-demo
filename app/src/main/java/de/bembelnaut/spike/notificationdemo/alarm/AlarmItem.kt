package de.bembelnaut.spike.notificationdemo.alarm

import java.time.LocalDateTime
import java.util.UUID

data class AlarmItem(
    val time: LocalDateTime,
    val title: String,
    val message: String,
    val taskId: UUID
)
