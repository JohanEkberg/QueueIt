package se.johan.queueit.ui.screens

import kotlinx.serialization.Serializable

/***
 * Below classes is to have type safe in navigation.
 */

@Serializable
object SplashScreenIdentifier

@Serializable
object HomeScreenIdentifier

@Serializable
object SettingsScreenIdentifier

@Serializable
data class AlbumScreenIdentifier(val albumId: Long)

@Serializable
data class ArtistScreenIdentifier(val artistId: Long)