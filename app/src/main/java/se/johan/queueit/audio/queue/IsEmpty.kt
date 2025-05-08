package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData
import java.util.Queue

class IsEmpty(private var _queue: Queue<AudioFileMetaData>) {
    @Throws(SongQueueException::class)
    operator fun invoke() : Boolean {
        return try {
            _queue.isEmpty()
        } catch(e: Exception) {
            throw SongQueueException("Failed to check if queue is empty, exception: ${e.message}")
        }
    }
}