package de.bembelnaut.spike.notificationdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.bembelnaut.spike.notificationdemo.ui.theme.NotificationDemoTheme
import java.time.LocalDate
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("TEST", "onCreate: entering")

        val service = RemindMeNotificationService(applicationContext)

        setContent {
            NotificationDemoTheme {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            service.showNotification(
                                "Test task 1",
                                uuid1,
                                LocalDate.now(),
                                "Hello World"
                            )
                        }
                    ) {
                        Text("Task 1")
                    }

                    Button(
                        onClick = {
                            service.showNotification(
                                "Test task 2",
                                uuid2,
                                LocalDate.now(),
                                "Hello World"
                            )
                        }
                    ) {
                        Text("Task 2")
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Log.i("TEST", "onNewIntent: entering")

        super.onNewIntent(intent)

        val taskName = intent?.getStringExtra("TASK_NAME") ?: "n/a"
        val taskId = intent?.getStringExtra("TASK_ID") ?: "n/a"
        val action = intent?.getStringExtra("ACTION") ?: "n/a"

        Log.i("TEST", "onNewIntent: action: $action")
        Log.i("TEST", "onNewIntent: task name: $taskName")
        Log.i("TEST", "onNewIntent: task id: $taskId")
    }

    companion object {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()
    }
}
