package se.johan.queueit.ui.screens

import kotlinx.serialization.Serializable

/***
 * Below classes is to have type safe in navigation.
 */
@Serializable
sealed class AppScreens(val route: String) {
    @Serializable
    data object SplashScreenIdentifier
        : AppScreens("se.johan.queueit.ui.screens.AppScreens.SplashScreenIdentifier")

    @Serializable
    data object HomeScreenIdentifier
        : AppScreens("se.johan.queueit.ui.screens.AppScreens.HomeScreenIdentifier")

    @Serializable
    data object SettingsScreenIdentifier
        : AppScreens("se.johan.queueit.ui.screens.AppScreens.SettingsScreenIdentifier")

    @Serializable
    data class AlbumScreenIdentifier(val albumId: Long)
        : AppScreens("se.johan.queueit.ui.screens.AppScreens.AlbumScreenIdentifier")

    @Serializable
    data class ArtistScreenIdentifier(val artistId: Long)
        : AppScreens("se.johan.queueit.ui.screens.AppScreens.ArtistScreenIdentifier")

    @Serializable
    data object SearchScreenIdentifier
        : AppScreens("se.johan.queueit.ui.screens.AppScreens.SearchScreenIdentifier")
}
