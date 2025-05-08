package se.johan.queueit.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import se.johan.queueit.ui.screens.AlbumScreen
import se.johan.queueit.ui.screens.AlbumScreenIdentifier
import se.johan.queueit.ui.screens.HomeScreen
import se.johan.queueit.ui.screens.HomeScreenIdentifier
import se.johan.queueit.ui.screens.SettingsScreen
import se.johan.queueit.ui.screens.SettingsScreenIdentifier
import se.johan.queueit.ui.screens.SplashScreen
import se.johan.queueit.ui.screens.SplashScreenIdentifier

@Composable
fun QueueItNavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        modifier = Modifier.padding(innerPadding),
        startDestination = SplashScreenIdentifier
    ) {
        // Map splash screen identifier with the splash screen
        composable<SplashScreenIdentifier> {
            SplashScreen(navController)
        }
        // Map home screen identifier with the home screen
        composable<HomeScreenIdentifier> {
            HomeScreen(navController)
        }
        // Map album screen identifier with the album screen
        composable<AlbumScreenIdentifier> {
            AlbumScreen(navController)
        }
        // Map settings screen identifier with the settings screen
        composable<SettingsScreenIdentifier> {
            SettingsScreen(navController)
        }
    }
}