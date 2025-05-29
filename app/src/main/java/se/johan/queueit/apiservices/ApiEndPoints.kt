package se.johan.queueit.apiservices

import javax.inject.Qualifier

object ApiEndPoints {
    const val LYRICS_BASE_URL = "https://api.lyrics.ovh/"
    const val ARTIST_OVERVIEW_BASE_URL = "https://ws.audioscrobbler.com/"
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ArtistOverviewApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LyricsApi

