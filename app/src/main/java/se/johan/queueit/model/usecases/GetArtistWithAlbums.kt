package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.ArtistWithAlbums
import se.johan.queueit.model.database.AudioDataDao

class GetArtistWithAlbums(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(artistId: Long): ArtistWithAlbums {
        return try {
            dao.getArtistWithAlbums(artistId)
        } catch(e: Exception) {
            throw AudioDataException("Failed to get artist with albums, exception: ${e.message}")
        }
    }
}