package uz.ttpu.sideeffectslab.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private const val TAG = "ImageScreen"

sealed class ImageUiState {
    object Loading : ImageUiState()
    data class Success(val url: String, val bytes: Int) : ImageUiState()
    data class Error(val message: String) : ImageUiState()
}

/**
 * Soxta rasm repozitoriyasi
 */
object FakeImageRepository {
    suspend fun load(url: String): Result<Int> {
        delay(500) // 500 ms kechikish
        return if (url.endsWith(".png", ignoreCase = true)) {
            Result.success(1024 * 45) // 45 KB
        } else {
            Result.failure(IllegalArgumentException("Faqat .png formatidagi rasmlar qo'llab-quvvatlanadi"))
        }
    }
}

/**
 * 6-topshiriq (Bonus): produceState
 *
 * Suspend funksiyani Compose State'ga aylantirish:
 * - URL o'zgarganda producer qayta ishga tushadi
 * - Composable tark etilganda producer bekor qilinadi (finally bloki orqali Logcat'ga yoziladi)
 * - Conflation: bir xil qiymat kelganda keraksiz recomposition bo'lmaydi
 */
@Composable
fun loadNetworkImage(url: String): State<ImageUiState> {
    return produceState<ImageUiState>(initialValue = ImageUiState.Loading, key1 = url) {
        value = ImageUiState.Loading
        Log.d(TAG, "produceState boshlandi: url = $url")
        try {
            val result = FakeImageRepository.load(url)
            value = if (result.isSuccess) {
                ImageUiState.Success(url, result.getOrNull() ?: 0)
            } else {
                ImageUiState.Error(result.exceptionOrNull()?.message ?: "Noma'lum xato")
            }
        } finally {
            Log.d(TAG, "produceState to'xtatildi yoki yakunlandi (finally): url = $url")
        }
    }
}

@Composable
fun ImageScreen() {
    var selectedUrl by remember { mutableStateOf("https://example.com/photo.png") }
    val imageState by loadNetworkImage(url = selectedUrl)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "produceState bilan rasm yuklash",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Tanlangan URL: $selectedUrl", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(24.dp))

        // Holatni UI da when orqali chiqarish
        when (val state = imageState) {
            is ImageUiState.Loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Rasm yuklanmoqda...", color = Color.Gray)
                }
            }
            is ImageUiState.Success -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Muvaffaqiyatli yuklandi!", color = Color(0xFF2E7D32), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            Text("Hajmi: ${state.bytes} bayt", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            is ImageUiState.Error -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = Color(0xFFC62828))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Xatolik yuz berdi!", color = Color(0xFFC62828), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            Text(state.message, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Button(onClick = { selectedUrl = "https://example.com/avatar_new.png" }) {
                Text(".png (Success)")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { selectedUrl = "https://example.com/document.pdf" },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
            ) {
                Text(".pdf (Error)")
            }
        }
    }
}
