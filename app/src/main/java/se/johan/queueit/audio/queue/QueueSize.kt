package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData
import java.util.Queue

class QueueSize(private var _queue: Queue<AudioFileMetaData>) {
    @Throws(SongQueueException::class)
    operator fun invoke() : Int {
        return try {
            _queue.size
        } catch(e: Exception) {
            throw SongQueueException("Failed to get queue size, exception: ${e.message}")
        }
    }
}