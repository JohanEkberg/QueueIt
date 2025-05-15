package se.johan.queueit.ui.screens

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import se.johan.queueit.ui.screens.components.TextFlowLeftToRight
import se.johan.queueit.ui.theme.blue100
import se.johan.queueit.ui.theme.blue250
import se.johan.queueit.ui.theme.blue80
import se.johan.queueit.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val appContext = context.applicationContext

    val successfulScan = settingsViewModel.successfulScan.collectAsState().value
    val pendingScan = settingsViewModel.pendingScan.collectAsState().value
    val artistsDetected = settingsViewModel.artistsDetected.collectAsState().value

    Scaffold { contentPadding ->
        if (successfulScan == true) {
            navController.navigate(AppScreens.HomeScreenIdentifier)
        }

        val swipeThreshold = 100f
        var offsetX by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            offsetX += dragAmount
                            if (offsetX < -swipeThreshold) {
                                navController.popBackStack()
                            }
                        }
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TextFlowLeftToRight(artistsDetected)
                Spacer(modifier = Modifier.height(300.dp))

                Button(
                    onClick = { settingsViewModel.doScan(appContext) },
                    enabled = !pendingScan,
                    shape = ButtonDefaults.shape,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        stringResource(R.string.settings_start_scan),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (pendingScan) {
                LinearProgressIndicator(
                    color = blue80,       // The bar (progress) color
                    trackColor = blue250, // The background track color
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(4.dp)
                )
            }
        }
    }
}