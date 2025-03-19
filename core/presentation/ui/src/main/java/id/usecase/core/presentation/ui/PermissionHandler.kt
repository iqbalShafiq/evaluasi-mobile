package id.usecase.core.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun NotificationPermissionHandler(
    onPermissionGranted: () -> Unit = {}
) {
    val context = LocalContext.current
    var showRationale by remember { mutableStateOf(false) }
    var openSettingsDialog by remember { mutableStateOf(false) }

    // Check if permission is required (only Android 13+)
    val permissionRequired = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        when {
            isGranted -> onPermissionGranted()
            shouldShowRationale(context) -> showRationale = true
            else -> openSettingsDialog = true
        }
    }

    // Check permission status when composable is first created
    LaunchedEffect(Unit) {
        if (!permissionRequired || isNotificationPermissionGranted(context)) {
            onPermissionGranted()
        } else {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Show rationale dialog
    if (showRationale) {
        AlertDialog(
            onDismissRequest = { showRationale = false },
            title = { Text("Notification Permission") },
            text = { Text("Aplikasi ini butuh permission notifikasi buat kasih update penting tentang kelas dan assessment.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            showRationale = false
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                ) {
                    Text("Coba Lagi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRationale = false }) {
                    Text("Skip")
                }
            }
        )
    }

    // Show settings dialog
    if (openSettingsDialog) {
        AlertDialog(
            onDismissRequest = { openSettingsDialog = false },
            title = { Text("Permission Required") },
            text = { Text("Kamu udah nolak permission notifikasi. Buka settings untuk ngasih permission?") },
            confirmButton = {
                TextButton(onClick = {
                    openSettingsDialog = false
                    openAppSettings(context)
                }) {
                    Text("Buka Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { openSettingsDialog = false }) {
                    Text("Nggak, Makasih")
                }
            }
        )
    }
}

// Helper functions
private fun isNotificationPermissionGranted(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // Versions below Android 13 don't need runtime permission
    }
}

private fun shouldShowRationale(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        context is androidx.activity.ComponentActivity
    ) {
        context.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
    } else false
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}