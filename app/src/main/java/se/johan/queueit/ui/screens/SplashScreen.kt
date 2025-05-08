package se.johan.queueit.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import se.johan.queueit.R
import se.johan.queueit.ui.screens.components.CheckPermissions
import se.johan.queueit.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val appContext = context.applicationContext
    Scaffold (
        snackbarHost = @Composable { SnackbarHost(snackbarHostState) }
    ){ contentPadding ->
        // Observe permission state
        //val isPermissionGranted by splashViewModel.isPermissionGranted
        val isPermissionGranted = splashViewModel.isPermissionGranted.collectAsState().value

        // Observe if scan is required
        //val requireScan by splashViewModel.requireScan
        val successfulStartup = splashViewModel.successfulStartup.collectAsState().value

        CheckPermissions(viewModel = splashViewModel)

        //Log.i(TAG, "isPermissionGranted: ${isPermissionGranted}")

        LaunchedEffect(isPermissionGranted) {
            if (isPermissionGranted) {
                splashViewModel.requireScan(appContext)
            }
        }

        val permissionNotGranted = stringResource(R.string.permissions_not_granted)
        LaunchedEffect(isPermissionGranted, successfulStartup) {
            if (isPermissionGranted && successfulStartup == true) {
                navController.navigate(HomeScreenIdentifier)
            } else if (isPermissionGranted && successfulStartup == false) {
                // TODO: Show error message, couldn't start the app
            } else if (!isPermissionGranted && successfulStartup != null) {
                snackbarHostState.showSnackbar(permissionNotGranted)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            StartLottieAnimation()
       }
   }
}

@Composable
fun StartLottieAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("splash.json"))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(200.dp).testTag(stringResource(R.string.splash_animation_tag))
    )
}