package se.johan.queueit.apiservices

import javax.inject.Qualifier

object ApiEndPoints {
    const val LYRICS_BASE_URL = "https://api.lyrics.ovh/"
}

//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class OverviewApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LyricsApi

