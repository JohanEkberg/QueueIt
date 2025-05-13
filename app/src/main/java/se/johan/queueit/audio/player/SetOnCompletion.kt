package se.johan.queueit.audio.player

class SetOnCompletion(private val repository: MusicPlayerRepository) {
    @Throws(MusicPlayerException::class)
    operator fun invoke(callback: () -> Unit) {
        return try {
            repository.setOnCompletion(callback)
        } catch(e: Exception) {
            throw MusicPlayerException("Failed to set song completion callback, exception: ${e.message}")
        }
    }
}