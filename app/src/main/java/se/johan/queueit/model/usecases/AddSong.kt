package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.AudioDataDao
import se.johan.queueit.model.database.SongEntity
import kotlin.jvm.Throws

class AddSong(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(song: SongEntity) {
        try {
            if (song.songName?.isNotEmpty() == true) {
                dao.insert(song)
            } else {
                throw AudioDataException("Song data is invalid")
            }
        } catch(e: Exception) {
            throw e
        }

    }
}