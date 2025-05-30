package se.johan.queueit.apiservices

import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtistOverviewRepository @Inject constructor(private val api: ArtistOverviewApiService) {
    private val gson = Gson()
    suspend fun getArtistOverview(artist: String): ArtistOverviewApiResult {
        return try {
            val response = api.requestArtistOverview(artist = artist)
            if (response.artist != null) {
                ArtistOverviewApiResult.Success(response.artist)
            } else {
                ArtistOverviewApiResult.Error(-1, "Unknown error — artist missing")
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = runCatching {
                gson.fromJson(errorBody, ErrorResponse::class.java)
            }.getOrNull()

            ArtistOverviewApiResult.Error(
                errorCode = errorResponse?.errorCode ?: e.code(),
                message = errorResponse?.message ?: "Unexpected error"
            )
        } catch (e: Exception) {
            ArtistOverviewApiResult.Error(-2, e.message ?: "Unknown exception")
        }
    }
}

