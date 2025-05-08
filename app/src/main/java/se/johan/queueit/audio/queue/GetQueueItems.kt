package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData
import java.util.LinkedList
import java.util.Queue

class GetQueueItems(private var _queue: Queue<AudioFileMetaData>) {
    @Throws(SongQueueException::class)
    operator fun invoke(): List<AudioFileMetaData> {
        return try {
            val queue: List<AudioFileMetaData> = LinkedList(_queue.map { it.copy() })
            queue
        } catch(e: Exception) {
            throw SongQueueException("Failed to get items from queue, exception: ${e.message}")
        }
    }
}