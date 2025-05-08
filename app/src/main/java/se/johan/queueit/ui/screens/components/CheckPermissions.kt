package se.johan.queueit.ui.screens.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import se.johan.queueit.viewmodel.SplashViewModel

@Composable
fun CheckPermissions(viewModel: SplashViewModel) {
    val context = LocalContext.current
    val permission = Manifest.permission.READ_MEDIA_AUDIO

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(permission)
        }
    }

    // Notify ViewModel only when `hasPermission` changes
    LaunchedEffect(hasPermission) {
        viewModel.updatePermissionStatus(hasPermission)
    }
}
