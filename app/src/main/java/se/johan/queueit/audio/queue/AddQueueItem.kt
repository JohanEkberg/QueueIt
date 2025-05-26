package se.johan.queueit.audio.queue

import android.util.Log
import se.johan.queueit.TAG
import se.johan.queueit.audio.data.AudioFileMetaData

class AddQueueItem(private var _songQueueRepository: SongQueueRepository) {
    operator fun invoke(song: AudioFileMetaData) : Boolean {
        Log.i(TAG,"Added song to queue: ${song.title}")
        return _songQueueRepository.add(song)
    }
}