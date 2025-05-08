package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData
import java.util.Queue

class IsEqual(private var _queue: Queue<AudioFileMetaData>) {
    @Throws(SongQueueException::class)
    operator fun invoke(songQueue: List<AudioFileMetaData>) : Boolean {
        return try {
            songQueue.zip(_queue.toList()).all { (a, b) -> a == b }
        } catch(e: Exception) {
            throw SongQueueException("Failed to check if queue is equal, exception: ${e.message}")
        }
    }
}