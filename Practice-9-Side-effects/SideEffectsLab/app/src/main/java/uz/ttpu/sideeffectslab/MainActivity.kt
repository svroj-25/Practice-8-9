package uz.ttpu.sideeffectslab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import uz.ttpu.sideeffectslab.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SideEffectsLabApp()
                }
            }
        }
    }
}

enum class SideEffectTab(val title: String, val icon: ImageVector) {
    SPLASH("1. Splash", Icons.Default.HourglassTop),
    SNACKBAR("2. Snackbar", Icons.Default.ChatBubble),
    LIFECYCLE("3. Lifecycle", Icons.Default.Sync),
    ANALYTICS("4. SideEffect", Icons.Default.Analytics),
    SCROLL("5. Scroll", Icons.Default.List),
    IMAGE("6. produceState", Icons.Default.Image)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideEffectsLabApp() {
    var selectedTab by remember { mutableStateOf(SideEffectTab.SPLASH) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SideEffectsLab: ${selectedTab.title}") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                SideEffectTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title.substringBefore(" ")) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                SideEffectTab.SPLASH -> LandingScreenParent()
                SideEffectTab.SNACKBAR -> SnackbarScreen()
                SideEffectTab.LIFECYCLE -> LifecycleScreen()
                SideEffectTab.ANALYTICS -> AnalyticsScreen()
                SideEffectTab.SCROLL -> ScrollScreen()
                SideEffectTab.IMAGE -> ImageScreen()
            }
        }
    }
}
