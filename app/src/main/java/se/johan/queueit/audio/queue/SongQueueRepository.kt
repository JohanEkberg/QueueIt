package se.johan.queueit.audio.queue

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import se.johan.queueit.audio.data.AudioFileMetaData
import java.util.LinkedList
import java.util.Queue
import javax.inject.Singleton

@Singleton
class SongQueueRepository {
    private val _queue: Queue<AudioFileMetaData> = LinkedList()
    private val queueItemsFlow = MutableStateFlow(_queue.toList())

    fun observe(): Flow<List<AudioFileMetaData>> = queueItemsFlow

    @Throws(SongQueueException::class)
    fun add(item: AudioFileMetaData) : Boolean {
        return try {
            _queue.add(item)
            emitQueue()
            true
        } catch(e: Exception) {
            throw SongQueueException("Failed to add item to queue, exception: ${e.message}")
        }
    }

    @Throws(SongQueueException::class)
    fun clear() : Boolean {
        return try {
            _queue.clear()
            emitQueue()
            true
        } catch(e: Exception) {
            throw SongQueueException("Failed to clear queue, exception: ${e.message}")
        }
    }

    @Throws(SongQueueException::class)
    fun getItem() : AudioFileMetaData? {
        return try {
            val item = _queue.poll()
            emitQueue()
            item
        } catch(e: Exception) {
            throw SongQueueException("Failed to get item from queue, exception: ${e.message}")
        }
    }

    @Throws(SongQueueException::class)
    fun getItems() : List<AudioFileMetaData> {
        return try {
            val queue: List<AudioFileMetaData> = LinkedList(_queue.map { it.copy() })
            queue
        } catch(e: Exception) {
            throw SongQueueException("Failed to get items from queue, exception: ${e.message}")
        }
    }

    @Throws(SongQueueException::class)
    fun isEmpty() : Boolean {
        return try {
            _queue.isEmpty()
        } catch(e: Exception) {
            throw SongQueueException("Failed to check if queue is empty, exception: ${e.message}")
        }
    }

    @Throws(SongQueueException::class)
    fun isEqual(songQueue: List<AudioFileMetaData>) : Boolean {
        return try {
            songQueue.zip(_queue.toList()).all { (a, b) -> a == b }
        } catch(e: Exception) {
            throw SongQueueException("Failed to do equal comparison, exception: ${e.message}")
        }
    }

    @Throws(SongQueueException::class)
    fun getSize() : Int {
        return try {
            _queue.size
        } catch(e: Exception) {
            throw SongQueueException("Failed to get queue size, exception: ${e.message}")
        }
    }

    @Throws(SongQueueException::class)
    fun remove(song: AudioFileMetaData) : Boolean {
        return try {
            _queue.remove(song)
            emitQueue()
            true
        } catch(e: Exception) {
            throw SongQueueException("Failed to remove item from queue, exception: ${e.message}")
        }
    }

    @Throws(SongQueueException::class)
    fun peek() : AudioFileMetaData? {
        return try {
            _queue.peek()
        } catch(e: Exception) {
            throw SongQueueException("Failed to peek queue, exception: ${e.message}")
        }
    }

    private fun emitQueue() {
        queueItemsFlow.value = _queue.toList()
    }

}