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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private const val TAG = "LandingScreen"

/**
 * 1-topshiriq: Splash ekrani (LaunchedEffect va rememberUpdatedState)
 *
 * Talab:
 * - LandingScreen(onTimeout) 2 soniya kutib onTimeout() ni bir marta chaqirsin.
 * - Ota composable'da version hisoblagichi har 500 ms da oshsin va yangi lambda yaratsin.
 * - Ikkita variant:
 *   1) Lambdani to'g'ridan-to'g'ri ushlaydigan (eski v0 versiyani ushlab qoladi).
 *   2) rememberUpdatedState ishlatadigan (eng so'nggi versiyani ushlaydi).
 *
 * IZOH (Kommentariy):
 * Agar LaunchedEffect(onTimeout) deb yozilsa, har 500 ms da yangi onTimeout kelgani sababli
 * effect to'xtab noldan qayta boshlanadi va 2 soniyalik kutish hech qachon tugamaydi!
 * Shu sababli kalit sifatida doimiy 'true' yoki 'Unit' berilib, lambda rememberUpdatedState bilan olinadi.
 */
@Composable
fun LandingScreenParent() {
    var version by remember { mutableIntStateOf(0) }
    var capturedLog by remember { mutableStateOf("Kutilmoqda...") }
    var updatedStateLog by remember { mutableStateOf("Kutilmoqda...") }

    // Ota composable'da har 500 ms da version oshadi
    LaunchedEffect(Unit) {
        while (version < 10) {
            delay(500)
            version++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ota composable versiyasi: v$version",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 1-variant: To'g'ridan-to'g'ri ushlash (Captured)
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1-variant: To'g'ridan-to'g'ri (Captured)",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
                Text(
                    text = "Natija: $capturedLog",
                    color = Color.DarkGray
                )
                DirectCaptureLanding(
                    onTimeout = {
                        val msg = "v$version (Eski v0 ushlandi)"
                        capturedLog = msg
                        Log.d(TAG, "DirectCapture onTimeout chaqirildi: $msg")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2-variant: rememberUpdatedState
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2-variant: rememberUpdatedState",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "Natija: $updatedStateLog",
                    color = Color.DarkGray
                )
                UpdatedStateLanding(
                    onTimeout = {
                        val msg = "v$version (Eng so'nggi versiya!)"
                        updatedStateLog = msg
                        Log.d(TAG, "UpdatedState onTimeout chaqirildi: $msg")
                    }
                )
            }
        }
    }
}

@Composable
private fun DirectCaptureLanding(onTimeout: () -> Unit) {
    // To'g'ridan-to'g'ri lambda ushlanadi (Captured stale lambda)
    LaunchedEffect(true) {
        delay(2000)
        onTimeout()
    }
}

@Composable
private fun UpdatedStateLanding(onTimeout: () -> Unit) {
    // rememberUpdatedState orqali eng so'nggi lambda olinadi
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    LaunchedEffect(true) {
        delay(2000)
        currentOnTimeout()
    }
}
