package de.bembelnaut.spike.notificationdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.bembelnaut.spike.notificationdemo.alarm.AlarmItem
import de.bembelnaut.spike.notificationdemo.alarm.AndroidAlarmScheduler
import de.bembelnaut.spike.notificationdemo.ui.theme.NotificationDemoTheme
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("TEST", "onCreate: entering")

        val alarmScheduler = AndroidAlarmScheduler(applicationContext)

        setContent {
            NotificationDemoTheme {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    // Create a alarm, that notifies me
                    Button(
                        onClick = {
                            val alarm1 = AlarmItem(
                                LocalDateTime.now(ZoneId.systemDefault()).plusSeconds(10L),
                                "Homework",
                                "Pls start your homework",
                                uuid1
                            )

                            alarmScheduler.schedule(alarm1)
                        }
                    ) {
                        Text("Create homework")
                    }

                    Button(
                        onClick = {
                            val alarm2 = AlarmItem(
                                LocalDateTime.now(ZoneId.systemDefault()).plusSeconds(20L),
                                "Test",
                                "Test is ready!",
                                uuid2
                            )

                            alarmScheduler.schedule(alarm2)
                        }
                    ) {
                        Text("Create test")
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Log.i("TEST", "onNewIntent: entering")

        super.onNewIntent(intent)

        intent?.run {
            Log.i("TEST", "onReceive: action: $action")

            val taskId = intent.getStringExtra("TASK_ID") ?: "n/a"
            Log.i("TEST", "onReceive: task id: $taskId")

            Toast.makeText(this@MainActivity, "$action: Start task: $taskId", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val uuid1: UUID = UUID.randomUUID()
        private val uuid2: UUID = UUID.randomUUID()


        const val START_TASK = "start_task"
    }
}
