package se.johan.queueit.audio.player

class SetOnProgress(private val repository: MusicPlayerRepository) {
    @Throws(MusicPlayerException::class)
    operator fun invoke(callback: (Int) -> Unit) {
        return try {
            repository.setOnProgress(callback)
        } catch(e: Exception) {
            throw MusicPlayerException("Failed to set progress callback, exception: ${e.message}")
        }
    }
}