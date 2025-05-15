package se.johan.queueit.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import se.johan.queueit.ui.screens.AlbumScreen
import se.johan.queueit.ui.screens.AppScreens
import se.johan.queueit.ui.screens.ArtistScreen
import se.johan.queueit.ui.screens.HomeScreen
import se.johan.queueit.ui.screens.SearchScreen
import se.johan.queueit.ui.screens.SettingsScreen
import se.johan.queueit.ui.screens.SplashScreen
import se.johan.queueit.viewmodel.SharedSearchViewModel

@Composable
fun QueueItNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    sharedSearchViewModel: SharedSearchViewModel
) {
    NavHost(
        navController = navController,
        modifier = Modifier.padding(innerPadding),
        startDestination = AppScreens.SplashScreenIdentifier
    ) {
        // Map splash screen identifier with the splash screen
        composable<AppScreens.SplashScreenIdentifier> {
            SplashScreen(navController)
        }
        // Map home screen identifier with the home screen
        composable<AppScreens.HomeScreenIdentifier> {
            HomeScreen(navController)
        }
        // Map album screen identifier with the album screen
        composable<AppScreens.AlbumScreenIdentifier> {
            AlbumScreen(navController)
        }
        // Map artist screen identifier with the artist screen
        composable<AppScreens.ArtistScreenIdentifier> {
            ArtistScreen(navController)
        }
        // Map settings screen identifier with the settings screen
        composable<AppScreens.SettingsScreenIdentifier> {
            SettingsScreen(navController)
        }

        // Map search screen identifier with the search screen
        composable<AppScreens.SearchScreenIdentifier> {
            SearchScreen(navController, sharedSearchViewModel)
        }
    }
}