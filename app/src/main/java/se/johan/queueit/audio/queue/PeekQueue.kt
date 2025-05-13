package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData
import java.util.Queue

class PeekQueue(private var _queue: Queue<AudioFileMetaData>) {
    @Throws(SongQueueException::class)
    operator fun invoke(): AudioFileMetaData? {
        return try {
            _queue.peek()
        } catch(e: Exception) {
            throw SongQueueException("Failed to peek oldest item in queue, exception: ${e.message}")
        }
    }
}