package se.johan.queueit.audio.player

class Stop (private val repository: MusicPlayerRepository) {
    @Throws(MusicPlayerException::class)
    operator fun invoke() {
        return try {
            repository.stop()
        } catch(e: Exception) {
            throw MusicPlayerException("Failed to stop song, exception: ${e.message}")
        }
    }
}