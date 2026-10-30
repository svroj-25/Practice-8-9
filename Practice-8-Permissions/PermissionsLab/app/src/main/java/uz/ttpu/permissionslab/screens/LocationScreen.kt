package uz.ttpu.permissionslab.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.WrongLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

enum class LocationStatus {
    PRECISE,      // Aniq (ACCESS_FINE_LOCATION)
    APPROXIMATE,  // Taxminiy (ACCESS_COARSE_LOCATION)
    DENIED        // Ruxsat yo'q
}

/**
 * 5-topshiriq: Bir martalik joylashuv ("Faqat bu safar")
 *
 * RequestMultiplePermissions orqali ACCESS_FINE_LOCATION va ACCESS_COARSE_LOCATION birga so'raladi.
 * ON_RESUME da holat doimiy yangilanib turadi ("Only this time" ruxsati fonga o'tganda bekor bo'ladi).
 */
@Composable
fun LocationScreen() {
    val context = LocalContext.current
    var locationStatus by remember { mutableStateOf(checkLocationPermissions(context)) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap: Map<String, Boolean> ->
        val fineGranted = permissionsMap[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissionsMap[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        locationStatus = when {
            fineGranted -> LocationStatus.PRECISE
            coarseGranted -> LocationStatus.APPROXIMATE
            else -> LocationStatus.DENIED
        }
    }

    // ON_RESUME orqali ruxsat fonga o'tib qaytganda bekor qilinganini aniqlash
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        locationStatus = checkLocationPermissions(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val (cardColor, icon, title, desc) = when (locationStatus) {
            LocationStatus.PRECISE -> Quadruple(
                Color(0xFFE8F5E9),
                Icons.Default.MyLocation,
                "ANIQ JOYLASHUV (PRECISE)",
                "ACCESS_FINE_LOCATION ruxsati berilgan. GPS koordinatalari aniq olinadi."
            )
            LocationStatus.APPROXIMATE -> Quadruple(
                Color(0xFFFFF8E1),
                Icons.Default.LocationOn,
                "TAXMINIY JOYLASHUV (COARSE)",
                "ACCESS_COARSE_LOCATION ruxsati berilgan. Shahar/rayon darajasida aniqlanadi."
            )
            LocationStatus.DENIED -> Quadruple(
                Color(0xFFFFEBEE),
                Icons.Default.WrongLocation,
                "JOYLASHUV RUXSATI YO'Q",
                "Joylashuvga ruxsat berilmagan yoki «Faqat bu safar» tanlanib ilova fonga o'tganida bekor bo'lgan."
            )
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = when (locationStatus) {
                        LocationStatus.PRECISE -> Color(0xFF2E7D32)
                        LocationStatus.APPROXIMATE -> Color(0xFFF57F17)
                        LocationStatus.DENIED -> Color(0xFFC62828)
                    },
                    modifier = Modifier.size(56.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (locationStatus) {
                        LocationStatus.PRECISE -> Color(0xFF2E7D32)
                        LocationStatus.APPROXIMATE -> Color(0xFFF57F17)
                        LocationStatus.DENIED -> Color(0xFFC62828)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = desc,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        launcher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                ) {
                    Text("Joylashuvni so'rash (Dialog)")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Eslatma: «Only this time» tanlansa, ilova fonga o'tganda tizim ruxsatni avtomat bekor qiladi. ON_RESUME buni ushlab oladi.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

private fun checkLocationPermissions(context: Context): LocationStatus {
    val fine = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarse = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return when {
        fine -> LocationStatus.PRECISE
        coarse -> LocationStatus.APPROXIMATE
        else -> LocationStatus.DENIED
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
