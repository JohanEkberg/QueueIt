package se.johan.queueit.audio.queue

import android.util.Log
import se.johan.queueit.TAG
import se.johan.queueit.audio.data.AudioFileMetaData
import java.util.Queue

class AddQueueItem(private var _queue: Queue<AudioFileMetaData>) {
    @Throws(SongQueueException::class)
    operator fun invoke(song: AudioFileMetaData) : Boolean {
        return try {
            Log.i(TAG,"Added song to queue: ${song.title}")
            _queue.add(song)
        } catch(e: Exception) {
            throw SongQueueException("Failed to add item to queue, exception: ${e.message}")
        }
    }
}