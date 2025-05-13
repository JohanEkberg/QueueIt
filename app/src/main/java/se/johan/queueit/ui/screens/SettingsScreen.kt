package se.johan.queueit.ui.screens

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import se.johan.queueit.R
import se.johan.queueit.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
    ) {
    val context = LocalContext.current
    val appContext = context.applicationContext
    Scaffold { contentPadding ->
        val successfulScan = settingsViewModel.successfulScan.collectAsState().value
        val pendingScan = settingsViewModel.pendingScan.collectAsState().value

        if (successfulScan) {
            navController.navigate(HomeScreenIdentifier)
        }

        val swipeThreshold = 100f
        var offsetX by remember { mutableStateOf(0f) }

        Column(modifier = Modifier
            .padding(contentPadding)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX += dragAmount
                    if (offsetX < -swipeThreshold) {
                        navController.popBackStack()
                    }
                }
            },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {settingsViewModel.doScan(appContext)},
                enabled = !pendingScan,
                shape = ButtonDefaults.shape,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,     // Background color
                    contentColor = MaterialTheme.colorScheme.onPrimary      // Text (content) color
                )
            ) {
                Text(
                    stringResource(R.string.settings_start_scan),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            if (pendingScan) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

//            Button(
//                onClick = {settingsViewModel.getArtistArtwork(appContext)},
//                enabled = true,
//                shape = ButtonDefaults.shape,
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primary,     // Background color
//                    contentColor = MaterialTheme.colorScheme.onPrimary      // Text (content) color
//                )
//            ) {
//                Text(
//                    stringResource(R.string.settings_get_artist_artwork),
//                    style = MaterialTheme.typography.labelLarge
//                )
//            }
        }
    }
}