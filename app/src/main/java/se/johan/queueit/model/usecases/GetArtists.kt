package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.ArtistEntity
import se.johan.queueit.model.database.AudioDataDao

class GetArtists(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(): List<ArtistEntity> {
        return try {
            dao.getArtists()
        } catch(e: Exception) {
            throw AudioDataException("Failed to get artists data, exception: ${e.message}")
        }
    }
}