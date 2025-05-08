package se.johan.queueit.audio.player

import android.content.Context

class Pause(private val repository: MusicPlayerRepository) {
    @Throws(MusicPlayerException::class)
    operator fun invoke() {
        return try {
            repository.pause()
        } catch(e: Exception) {
            throw MusicPlayerException("Failed to pause song, exception: ${e.message}")
        }
    }
}