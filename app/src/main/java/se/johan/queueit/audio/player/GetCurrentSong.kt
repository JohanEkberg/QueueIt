package se.johan.queueit.audio.player

import se.johan.queueit.audio.data.AudioFileMetaData

class GetCurrentSong(private val repository: MusicPlayerRepository) {
    @Throws(MusicPlayerException::class)
    operator fun invoke() : AudioFileMetaData? {
        return try {
            repository.getCurrentSong()
        } catch(e: Exception) {
            throw MusicPlayerException("Failed to get current song, exception: ${e.message}")
        }
    }
}