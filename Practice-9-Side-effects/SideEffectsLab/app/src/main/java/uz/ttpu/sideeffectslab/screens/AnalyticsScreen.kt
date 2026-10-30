package uz.ttpu.sideeffectslab.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val TAG = "AnalyticsScreen"

/**
 * Tashqi Analytics obyekti (Compose boshqarmaydigan tashqi obyekt)
 */
object FakeAnalytics {
    var userType: String = "Guest"
}

/**
 * 4-topshiriq: SideEffect va analytics
 *
 * IZOH VA TAHLIL:
 * Holat o'zgarmasa ham (masalan 'Ortiqcha recomposition' tugmasi bosilganda) SideEffect ishlaydi.
 * Sababi: SideEffect hech qanday kalit qabul qilmaydi va har bir muvaffaqiyatli recomposition / commit'dan
 * keyin doimiy ravishda bajariladi.
 *
 * ATTEMPT MODELI:
 * Compose recomposition urinishi bekor qilinishi (discard) mumkin; to'g'ridan-to'g'ri chaqiruv
 * commits=false da ham ishlab xato log jo'natadi, ammo SideEffect faqat commits=true
 * (chizish to'liq muvaffaqiyatli bo'lganda) ishlaydi.
 */
@Composable
fun AnalyticsScreen() {
    var userType by remember { mutableStateOf("Guest") }
    var dummyCounter by remember { mutableIntStateOf(0) }
    var sideEffectExecutionCount by remember { mutableIntStateOf(0) }

    // SideEffect: Compose holatini tashqi obyektga xavfsiz uzatish
    SideEffect {
        FakeAnalytics.userType = userType
        sideEffectExecutionCount++
        Log.d(TAG, "SideEffect ishladi! Soni: $sideEffectExecutionCount, Analytics.userType = ${FakeAnalytics.userType}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SideEffect va Tashqi Analytics",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Joriy userType: $userType",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "FakeAnalytics.userType: ${FakeAnalytics.userType}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "SideEffect jami ishlash soni: $sideEffectExecutionCount",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(onClick = { userType = "Regular User" }) {
                Text("Regular")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { userType = "Premium VIP" }) {
                Text("VIP")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { userType = "Admin" }) {
                Text("Admin")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // userType o'zgarmasdan shunchaki recomposition keltirib chiqarish
        OutlinedButton(onClick = { dummyCounter++ }) {
            Text("Recomposition qo'zg'atish (dummy: $dummyCounter)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Attempt modeli: SideEffect faqat muvaffaqiyatli commit'dan keyin ishlaydi, bekor bo'lgan recomposition'larda kutilmagan ta'sir bermaydi.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
