package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.ArtistEntity
import se.johan.queueit.model.database.AudioDataDao

class GetArtistById(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(artistId: Int): ArtistEntity {
        return try {
            dao.getArtistById(artistId)
        } catch(e: Exception) {
            throw AudioDataException("Failed to get artist by id, exception: ${e.message}")
        }
    }
}