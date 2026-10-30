package uz.ttpu.sideeffectslab.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

private const val TAG = "LifecycleLogger"

/**
 * 3-topshiriq: Lifecycle logger (DisposableEffect)
 *
 * Listener qo'shish va tozalash:
 * - lifecycleOwner kalit qilinadi
 * - onDispose ichida removeObserver chaqiriladi va log yoziladi
 * - Switch orqali observer ko'rsatiladi / yashiriladi
 */
@Composable
fun LifecycleScreen() {
    var isObserverActive by remember { mutableStateOf(true) }
    var eventLogs by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "DisposableEffect va Lifecycle kuzatuvchisi",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Kuzatuvchi holati (Switch):")
            Spacer(modifier = Modifier.width(12.dp))
            Switch(
                checked = isObserverActive,
                onCheckedChange = { isObserverActive = it }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isObserverActive) {
            LifecycleObserverComposable(
                onLog = { logMsg ->
                    eventLogs = (eventLogs + logMsg).takeLast(6)
                }
            )
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Composable tark etildi: observer onDispose orqali tozalandi.",
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFFC62828)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Logcat yozuvlari:", style = MaterialTheme.typography.labelLarge)
        eventLogs.forEach { log ->
            Text(log, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
        }
    }
}

@Composable
fun LifecycleObserverComposable(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onLog: (String) -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START || event == Lifecycle.Event.ON_STOP) {
                val msg = "Hodisa: $event"
                Log.d(TAG, msg)
                onLog(msg)
            }
        }

        Log.d(TAG, "addObserver chaqirildi (Observer qo'shildi)")
        onLog("addObserver (qo'shildi)")
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            Log.d(TAG, "removeObserver chaqirildi (Observer olib tashlandi)")
            onLog("removeObserver (tozalandi)")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Observer faol ishlab turibdi", color = Color(0xFF2E7D32))
            Text("ON_START va ON_STOP Logcat'ga yozilmoqda.", style = MaterialTheme.typography.bodySmall)
        }
    }
}
