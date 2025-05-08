package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.AudioDataDao
import se.johan.queueit.model.database.SongEntity

class GetSongs(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(): List<SongEntity> {
        return try {
            dao.getSongs()
        } catch(e: Exception) {
            throw AudioDataException("Failed to get songs, exception: ${e.message}")
        }
    }
}