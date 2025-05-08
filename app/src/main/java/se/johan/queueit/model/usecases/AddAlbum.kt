package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.AudioDataDao
import kotlin.jvm.Throws

class AddAlbum(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(album: AlbumEntity) : Long {
        try {
            if (album.albumName?.isNotEmpty() == true) {
                return dao.insert(album)
            } else {
                throw AudioDataException("Album data is invalid")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}