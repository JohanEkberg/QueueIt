package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.AudioDataDao
import se.johan.queueit.model.database.SongEntity

class GetSongByName(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(songName: String): List<SongEntity> {
        return try {
            dao.getSongByName(songName)
        } catch(e: Exception) {
            throw AudioDataException("Failed to get song by name, requested song: ${songName}, exception: ${e.message}")
        }
    }
}