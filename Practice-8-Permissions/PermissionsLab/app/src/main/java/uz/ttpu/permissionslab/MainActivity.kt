package uz.ttpu.permissionslab

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
import uz.ttpu.permissionslab.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PermissionsLabApp()
                }
            }
        }
    }
}

enum class PermissionsTab(val title: String, val icon: ImageVector) {
    CONNECTIVITY("Normal (Tarmoq)", Icons.Default.Wifi),
    CAMERA("Kamera (3 holat)", Icons.Default.CameraAlt),
    OVERLAY("Overlay (Special)", Icons.Default.Layers),
    LOCATION("Joylashuv (1 martalik)", Icons.Default.LocationOn),
    NOTIFICATIONS("Bildirishnoma (API 33+)", Icons.Default.Notifications)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsLabApp() {
    var selectedTab by remember { mutableStateOf(PermissionsTab.CONNECTIVITY) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PermissionsLab: ${selectedTab.title}") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                PermissionsTab.values().forEach { tab ->
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
                PermissionsTab.CONNECTIVITY -> ConnectivityScreen()
                PermissionsTab.CAMERA -> CameraScreen()
                PermissionsTab.OVERLAY -> OverlayScreen()
                PermissionsTab.LOCATION -> LocationScreen()
                PermissionsTab.NOTIFICATIONS -> NotificationScreen()
            }
        }
    }
}
