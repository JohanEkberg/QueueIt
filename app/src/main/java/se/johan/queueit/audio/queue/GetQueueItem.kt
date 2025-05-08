package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData
import java.util.Queue

class GetQueueItem(private var _queue: Queue<AudioFileMetaData>) {
    @Throws(SongQueueException::class)
    operator fun invoke(): AudioFileMetaData? {
        return try {
            _queue.poll()
        } catch(e: Exception) {
            throw SongQueueException("Failed to get item from queue, exception: ${e.message}")
        }
    }
}