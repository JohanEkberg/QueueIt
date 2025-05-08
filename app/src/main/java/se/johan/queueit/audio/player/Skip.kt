package se.johan.queueit.audio.player

import android.content.Context

class Skip(private val repository: MusicPlayerRepository) {
    @Throws(MusicPlayerException::class)
    operator fun invoke(context: Context) {
        return try {
            repository.skip(context)
        } catch(e: Exception) {
            throw MusicPlayerException("Failed to skip song, exception: ${e.message}")
        }
    }
}