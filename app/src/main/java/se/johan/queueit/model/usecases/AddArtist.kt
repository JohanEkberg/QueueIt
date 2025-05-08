package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.ArtistEntity
import se.johan.queueit.model.database.AudioDataDao
import kotlin.jvm.Throws

class AddArtist(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(artist: ArtistEntity) : Long {
        try {
            if (artist.artistName?.isNotEmpty() == true) {
                return dao.insert(artist)
            } else {
                throw AudioDataException("Artist data is invalid")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}