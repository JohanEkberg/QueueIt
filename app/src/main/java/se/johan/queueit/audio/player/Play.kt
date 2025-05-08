package se.johan.queueit.audio.player

import android.content.Context

class Play(private val repository: MusicPlayerRepository) {
    @Throws(MusicPlayerException::class)
    operator fun invoke(context: Context) {
        return try {
            repository.play(context)
        } catch(e: Exception) {
            throw MusicPlayerException("Failed to play song, exception: ${e.message}")
        }
    }
}