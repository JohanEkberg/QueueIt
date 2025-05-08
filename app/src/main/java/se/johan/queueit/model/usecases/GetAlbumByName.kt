package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.AudioDataDao

class GetAlbumByName(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(albumName: String): List<AlbumEntity> {
        return try {
            dao.getAlbumByName(albumName)
        } catch(e: Exception) {
            throw AudioDataException("Failed to get album by name, requested album: ${albumName}, exception: ${e.message}")
        }
    }
}