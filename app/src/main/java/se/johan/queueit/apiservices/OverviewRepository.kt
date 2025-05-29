package se.johan.queueit.apiservices

import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverviewRepository @Inject constructor(private val api: OverviewApiService) {
    private val gson = Gson()
    suspend fun getArtistOverview(artist: String): OverviewApiResult {
        return try {
            val response = api.requestArtistOverview(artist = artist)
            if (response.artist != null) {
                OverviewApiResult.Success(response.artist)
            } else {
                OverviewApiResult.Error(-1, "Unknown error — artist missing")
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = runCatching {
                gson.fromJson(errorBody, ErrorResponse::class.java)
            }.getOrNull()

            OverviewApiResult.Error(
                errorCode = errorResponse?.errorCode ?: e.code(),
                message = errorResponse?.message ?: "Unexpected error"
            )
        } catch (e: Exception) {
            OverviewApiResult.Error(-2, e.message ?: "Unknown exception")
        }
    }
}

