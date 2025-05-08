package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData
import java.util.Queue

class ClearQueue(private var _queue: Queue<AudioFileMetaData>) {
    @Throws(SongQueueException::class)
    operator fun invoke() : Boolean {
        return try {
            _queue.clear()
            true
        } catch(e: Exception) {
            throw SongQueueException("Failed to remove item from queue, exception: ${e.message}")
        }
    }
}