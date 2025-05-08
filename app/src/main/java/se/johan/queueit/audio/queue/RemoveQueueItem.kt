package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData
import java.util.Queue

class RemoveQueueItem(private var _queue: Queue<AudioFileMetaData>) {
    @Throws(SongQueueException::class)
    operator fun invoke(song: AudioFileMetaData) : Boolean {
        return try {
            _queue.remove(song)
        } catch(e: Exception) {
            throw SongQueueException("Failed to remove item from queue, exception: ${e.message}")
        }
    }
}