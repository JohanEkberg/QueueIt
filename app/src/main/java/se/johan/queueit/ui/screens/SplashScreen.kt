package se.johan.queueit.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import se.johan.queueit.R
import se.johan.queueit.ui.screens.components.CheckPermissions
import se.johan.queueit.ui.screens.components.TextFlowRightToLeft
import se.johan.queueit.ui.theme.Gray600
import se.johan.queueit.ui.theme.Gray800
import se.johan.queueit.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val appContext = context.applicationContext
    Scaffold (
        snackbarHost = @Composable { SnackbarHost(snackbarHostState) }
    ){ contentPadding ->
        // Observe permission state
        val isPermissionGranted = splashViewModel.isPermissionGranted.collectAsState().value

        // Observe if scan is required
        val successfulStartup = splashViewModel.successfulScan.collectAsState().value

        // Observe when an artist has been detected during scan
        val artistsDetected = splashViewModel.artistsDetected.collectAsState().value

        CheckPermissions(viewModel = splashViewModel)

        //Log.i(TAG, "isPermissionGranted: ${isPermissionGranted}")

        LaunchedEffect(isPermissionGranted) {
            if (isPermissionGranted) {
                splashViewModel.requireScan(appContext)
            }
        }

        val permissionNotGranted = stringResource(R.string.permissions_not_granted)
        val error = stringResource(R.string.splash_error_message)
        LaunchedEffect(isPermissionGranted, successfulStartup) {
            if (isPermissionGranted && successfulStartup == true) {
                navController.navigate(AppScreens.HomeScreenIdentifier)
            } else if (isPermissionGranted && successfulStartup == false) {
                errorMessage = error
            } else if (!isPermissionGranted && successfulStartup != null) {
                snackbarHostState.showSnackbar(permissionNotGranted)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.splash_scanning_message),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Gray600
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextFlowRightToLeft(artistsDetected)
                    Spacer(modifier = Modifier.height(30.dp))
                    StartLottieAnimation()
                }
            }
        }

   }
}

@Composable
fun StartLottieAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("splash.json"))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(400.dp).testTag(stringResource(R.string.splash_animation_tag))
    )
}