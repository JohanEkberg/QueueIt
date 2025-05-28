package se.johan.queueit.apiservices

import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LyricsRepository @Inject constructor(private val api: LyricApiService) {
    suspend fun getLyric(artist: String, title: String): LyricsResponse {
        return try {
            api.requestSongLyric(artist = artist, title = title)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                throw ApiServicesException(
                    "Lyrics not found for artist: $artist and title: $title",
                    errorCode = ApiServicesErrorCode.LYRIC_NOT_FOUND.ordinal
                )
            } else {
                throw e // rethrow other HTTP errors
            }
        }
    }
}

