package se.johan.queueit.apiservices

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query
import se.johan.queueit.BuildConfig

interface ArtistOverviewApiService {
    @GET("2.0/")
    suspend fun requestArtistOverview(
        @Query(OverviewApiQueryParameters.METHOD_KEY) method: String = OverviewApiQueryParameters.METHOD_VALUE,
        @Query(OverviewApiQueryParameters.ARTIST_KEY) artist: String,
        @Query(OverviewApiQueryParameters.API_KEY) apiKey: String = BuildConfig.LASTFM_API_KEY,
        @Query(OverviewApiQueryParameters.FORMAT_KEY) format: String = OverviewApiQueryParameters.FORMAT_VALUE
    ): ArtistOverviewResponse
}

sealed class ArtistOverviewApiResult {
    data class Success(val artistInfo: ArtistInfo) : ArtistOverviewApiResult()
    data class Error(val errorCode: Int, val message: String) : ArtistOverviewApiResult()
}

data class ArtistOverviewResponse(
    @SerializedName("artist") val artist: ArtistInfo?
)

data class ArtistInfo(
    @SerializedName("name") val name: String = "",
    @SerializedName("bio") val bio: ArtistBio = ArtistBio()
)

data class ArtistBio(
    @SerializedName("summary") val summary: String = ""
)

data class ErrorResponse(
    @SerializedName("error") val errorCode: Int,
    @SerializedName("message") val message: String
)