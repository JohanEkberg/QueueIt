package se.johan.queueit.apiservices

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import se.johan.queueit.apiservices.LyricsApiQueryParameters.ARTIST_KEY
import se.johan.queueit.apiservices.LyricsApiQueryParameters.TITLE_KEY

interface LyricApiService {
    @Headers("Accept: plain/text")
    @GET("v1/{$ARTIST_KEY}/{$TITLE_KEY}")
    suspend fun requestSongLyric(@Path(ARTIST_KEY) artist: String, @Path(TITLE_KEY) title: String): LyricsResponse
}

data class LyricsResponse(
    @SerializedName("lyrics") val lyrics: String?
)

sealed class LyricsApiResult {
    data class Success(val lyric: String) : LyricsApiResult()
    data class Error(val errorCode: Int, val message: String) : LyricsApiResult()
}