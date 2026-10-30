package uz.ttpu.sideeffectslab.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "ScrollScreen"

/**
 * 5-topshiriq: Scroll: derivedStateOf va snapshotFlow
 *
 * 1) derivedStateOf: showButton holati faqat listState.firstVisibleItemIndex > 0
 *    o'zgargandagina recomposition chaqiradi.
 * 2) Taqqoslash: To'g'ridan-to'g'ri listState.firstVisibleItemIndex > 0 deb o'qilganda
 *    har bir yangi element ko'ringanda recomposition bo'ladi.
 * 3) snapshotFlow: ro'yxat 0-elementdan pastga o'tganda faqat bitta analytics xabari jo'natiladi.
 */
@Composable
fun ScrollScreen() {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // 1-variant: derivedStateOf orqali optimallashtirilgan holat
    val showButtonDerived by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    // 2-variant: To'g'ridan-to'g'ri o'qish (taqqoslash uchun)
    val showButtonDirect = listState.firstVisibleItemIndex > 0

    var derivedRecompositions by remember { mutableIntStateOf(0) }
    var directRecompositions by remember { mutableIntStateOf(0) }
    var analyticsEventsCount by remember { mutableIntStateOf(0) }

    // Recomposition hisoblagichlari
    SideEffect {
        directRecompositions++
    }

    // snapshotFlow orqali 0-elementdan o'tganda faqat bitta hodisa yuborish
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index > 0 }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                analyticsEventsCount++
                Log.d(TAG, "Analytics yuborildi: Foydalanuvchi 0-elementdan o'tdi (Jami: $analyticsEventsCount)")
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Yuqori statistika paneli
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Birinchi ko'ringan indeks: ${listState.firstVisibleItemIndex}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "To'g'ridan-to'g'ri recomposition soni: $directRecompositions",
                        color = Color(0xFFC62828),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "derivedStateOf holati: $showButtonDerived",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "snapshotFlow hodisalari: $analyticsEventsCount",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // 100 elementli ro'yxat
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f)
            ) {
                items(100) { index ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Element #$index",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        // derivedStateOf bilan ko'rsatiladigan "Tepaga qaytish" tugmasi
        if (showButtonDerived) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Tepaga")
            }
        }
    }
}
