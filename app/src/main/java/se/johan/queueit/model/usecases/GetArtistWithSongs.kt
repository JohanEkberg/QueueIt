package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.ArtistWithSongs
import se.johan.queueit.model.database.AudioDataDao

class GetArtistWithSongs(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(artistId: Long): ArtistWithSongs {
        return try {
            dao.getArtistWithSongs(artistId)
        } catch(e: Exception) {
            throw AudioDataException("Failed to get artist with songs, exception: ${e.message}")
        }
    }
}