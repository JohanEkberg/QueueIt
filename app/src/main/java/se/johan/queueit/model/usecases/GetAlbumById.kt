package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.AudioDataDao

class GetAlbumById(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(albumId: Int): AlbumEntity {
        return try {
            dao.getAlbumById(albumId)
        } catch(e: Exception) {
            throw AudioDataException("Failed to get album, exception: ${e.message}")
        }
    }
}