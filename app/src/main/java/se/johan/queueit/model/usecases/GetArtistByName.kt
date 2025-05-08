package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.ArtistEntity
import se.johan.queueit.model.database.AudioDataDao

class GetArtistByName(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(artistName: String): List<ArtistEntity> {
        return try {
            dao.getArtistByName(artistName)
        } catch(e: Exception) {
            throw AudioDataException("Failed to get artist by name, requested artist: ${artistName}, exception: ${e.message}")
        }
    }
}