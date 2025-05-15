package se.johan.queueit.ui.navigation

import se.johan.queueit.ui.screens.AppScreens

fun resolveScreen(route: String?): AppScreens? {
    return when (route) {
        AppScreens.SplashScreenIdentifier.route -> AppScreens.SplashScreenIdentifier
        AppScreens.HomeScreenIdentifier.route -> AppScreens.HomeScreenIdentifier
        AppScreens.SearchScreenIdentifier.route -> AppScreens.SearchScreenIdentifier
        AppScreens.SettingsScreenIdentifier.route -> AppScreens.SettingsScreenIdentifier
        else -> null
    }
}