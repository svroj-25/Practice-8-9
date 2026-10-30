package uz.ttpu.permissionslab.screens

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 2-topshiriq: Normal ruxsatnomalar (INTERNET va ACCESS_NETWORK_STATE)
 *
 * IZOH (Kommentariy):
 * INTERNET va ACCESS_NETWORK_STATE normal ruxsatnomalar bo'lib, foydalanuvchining shaxsiy
 * xavfsizligiga yoki maxfiy ma'lumotlariga xavf tug'dirmagani uchun ularga hech qanday
 * runtime dialog va dinamik so'rov kerak emas — tizim o'rnatish vaqtidayoq avtomat tarzda ruxsat beradi.
 */
@Composable
fun ConnectivityScreen() {
    val context = LocalContext.current
    var isOnline by remember { mutableStateOf(checkNetworkAvailability(context)) }

    // ConnectivityManager orqali tarmoq o'zgarishini real vaqtda kuzatish
    DisposableEffect(context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isOnline = true
            }

            override fun onLost(network: Network) {
                isOnline = false
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        onDispose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isOnline) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff,
                    contentDescription = null,
                    tint = if (isOnline) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isOnline) "ONLINE" else "OFFLINE",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOnline) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isOnline) "Tarmoqqa muvaffaqiyatli ulangan" else "Internet aloqasi mavjud emas (yoki Samolyot rejimi yoqilgan)",
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Normal ruxsatnomalarga runtime dialog kerak emas. Tizim o'rnatish paytida bervoradi.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

private fun checkNetworkAvailability(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNet = cm.activeNetwork ?: return false
    val caps = cm.getNetworkCapabilities(activeNet) ?: return false
    return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
