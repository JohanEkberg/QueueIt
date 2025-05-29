package se.johan.queueit.apiservices

data class ApiServicesUseCases(
    val getLyric: GetLyric,
    val getArtistOverview: GetArtistOverview,
)

class ApiServicesException(
    message: String = "",
    val errorCode: Int = 0
) : Exception(message)

enum class ApiServicesErrorCode(value: Int) {
    LYRIC_NOT_FOUND(10),
    ARTIST_NOT_FOUND(20)
}
