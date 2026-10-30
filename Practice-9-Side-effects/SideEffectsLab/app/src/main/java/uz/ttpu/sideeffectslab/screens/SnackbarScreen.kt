package uz.ttpu.sideeffectslab.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private const val TAG = "SnackbarScreen"

/**
 * 2-topshiriq: Snackbar tugmasi (rememberCoroutineScope)
 *
 * IZOH (Kommentariy):
 * Nega LaunchedEffect bu yerda ishlamaydi?
 * Chunki LaunchedEffect bu @Composable funksiya hisoblanadi. Uni Button'ning
 * onClick kabi oddiy callback'lari ichidan chaqirib bo'lmaydi (kompilyatsiya xatosi beradi).
 * Foydalanuvchi tugma bosganda boshlanadigan asinxron ishlar uchun 'rememberCoroutineScope'
 * orqali korutina ishga tushiriladi (scope.launch).
 *
 * KUZATUV:
 * Snackbar tugamasdan oldin ikkinchi marta bosilsa, birinchi snackbar bekor qilinadi
 * va yangisi darhol uning o'rniga chiqadi (chunki SnackbarHost navbat asosida yangisini chiqaradi).
 */
@Composable
fun SnackbarScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var clickCount by remember { mutableIntStateOf(0) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Snackbar va Coroutine Scope",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bosishlar soni: $clickCount",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    clickCount++
                    Log.d(TAG, "Tugma bosildi: #$clickCount")
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Xabar #$clickCount ko'rsatildi!",
                            actionLabel = "OK",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            ) {
                Text("Snackbar chiqarish")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tugma onClick callback ichidan scope.launch chaqiriladi.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
