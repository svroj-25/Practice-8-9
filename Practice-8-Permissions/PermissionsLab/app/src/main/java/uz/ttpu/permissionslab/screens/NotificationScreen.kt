package uz.ttpu.permissionslab.screens

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * 6-topshiriq (Bonus): Bildirishnoma ruxsatnomasi (POST_NOTIFICATIONS)
 *
 * Android 13 (API 33)+ qurilmalarda POST_NOTIFICATIONS runtime ruxsatnoma sifatida so'raladi.
 * API 32 va undan past versiyalarda esa tizimda sukut bo'yicha yoqilgan bo'ladi va so'rov o'tkazib yuboriladi.
 */
@Composable
fun NotificationScreen() {
    val context = LocalContext.current

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true // API 32 va undan pastda avtomat ruxsat bor
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            sendTestNotification(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (hasNotificationPermission) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (hasNotificationPermission) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                    contentDescription = null,
                    tint = if (hasNotificationPermission) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.size(56.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (hasNotificationPermission) "Bildirishnomalar ruxsat etilgan" else "Bildirishnoma ruxsati kerak",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (hasNotificationPermission) Color(0xFF2E7D32) else Color(0xFFC62828)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Qurilma Android versiyasi: API ${Build.VERSION.SDK_INT} (${if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) "Android 13+ runtime talab qilinadi" else "Android 12 va undan past, avtomatik ruxsat mavjud"}).",
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Button(
                        onClick = {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    ) {
                        Text("POST_NOTIFICATIONS ruxsatini so'rash")
                    }
                } else {
                    Button(
                        onClick = {
                            sendTestNotification(context)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Test bildirishnoma yuborish")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "API 32 emulyatorida dialog chiqmaydi (sukut bo'yicha ruxsat bor). API 34 da esa tizim dialogi chiqadi.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

private const val CHANNEL_ID = "permissions_lab_channel"

private fun sendTestNotification(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "PermissionsLab Test",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "PermissionsLab test bildirishnomalari"
        }
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("PermissionsLab Test Bildirishnomasi")
        .setContentText("Ruxsatnoma muvaffaqiyatli tekshirildi va ishlamoqda!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
    ) {
        NotificationManagerCompat.from(context).notify(101, notification)
    }
}
