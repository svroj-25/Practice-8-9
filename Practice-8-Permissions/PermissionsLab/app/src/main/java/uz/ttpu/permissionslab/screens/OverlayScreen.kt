package uz.ttpu.permissionslab.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

/**
 * 4-topshiriq: Special ruxsatnoma: SYSTEM_ALERT_WINDOW (Overlay)
 *
 * Switch bosilganda tizim sozlamalari ochiladi.
 * LifecycleEventEffect(Lifecycle.Event.ON_RESUME) orqali sozlamalardan
 * qaytgan vaqtda Settings.canDrawOverlays(context) tekshirilib, Switch yangilanadi.
 */
@Composable
fun OverlayScreen() {
    val context = LocalContext.current

    var canDraw by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(context)
            } else true
        )
    }

    // "Tekshir -> Sozlamalarni och -> qaytganda yana tekshir" andozasi
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        canDraw = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else true
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
                containerColor = if (canDraw) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = null,
                    tint = if (canDraw) Color(0xFF2E7D32) else Color(0xFFE65100),
                    modifier = Modifier.size(56.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Boshqa ilovalar ustidan chizish",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (canDraw) Color(0xFF2E7D32) else Color(0xFFE65100)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (canDraw)
                        "Overlay ruxsati berilgan. Ilova suzuvchi oynalar chiqara oladi."
                    else
                        "Ruxsat yo'q. Switch tugmasini yoqing va tizim sozlamalaridan ruxsat bering.",
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (canDraw) "Ruxsat holati: YOQILGAN" else "Ruxsat holati: O'CHIRILGAN",
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = canDraw,
                        onCheckedChange = {
                            openOverlaySettings(context)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Sozlamalardan qaytganda hech qanday callback kelmagani sababli, ON_RESUME da holat qayta tekshiriladi.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

private fun openOverlaySettings(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
        context.startActivity(intent)
    }
}
