package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.AlbumWithSongs
import se.johan.queueit.model.database.AudioDataDao

class GetAlbumSongs(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(albumId: Long): AlbumWithSongs {
        return try {
            dao.getAlbumWithSongs(albumId)
        } catch(e: Exception) {
            throw AudioDataException("Failed to get album songs, exception: ${e.message}")
        }
    }
}