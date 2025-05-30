package se.johan.queueit.apiservices

class GetLyric(private val repository: LyricsRepository) {
    suspend operator fun invoke(artist: String, title: String) : LyricsApiResult {
        return repository.getLyric(artist, title)
    }
}