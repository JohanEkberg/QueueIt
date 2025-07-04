package se.johan.queueit.apiservices

import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LyricsRepository @Inject constructor(private val api: LyricApiService) {
    private val gson = Gson()

    suspend fun getLyric(artist: String, title: String): LyricsApiResult {
        return try {
            val response = api.requestSongLyric(artist = artist, title = title)
            if (response.lyrics != null) {
                LyricsApiResult.Success(response.lyrics)
            } else {
                LyricsApiResult.Error(-1, "Unknown error — lyric missing")
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = runCatching {
                gson.fromJson(errorBody, ErrorResponse::class.java)
            }.getOrNull()

            LyricsApiResult.Error(
                errorCode = errorResponse?.errorCode ?: e.code(),
                message = errorResponse?.message ?: "Unexpected error"
            )
        } catch (e: Exception) {
            LyricsApiResult.Error(-2, e.message ?: "Unknown exception")
        }
    }
}

