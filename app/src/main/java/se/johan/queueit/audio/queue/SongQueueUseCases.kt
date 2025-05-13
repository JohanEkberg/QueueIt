package se.johan.queueit.audio.queue

data class SongQueueUseCases (
    val getQueueItem: GetQueueItem,
    val getQueueItems: GetQueueItems,
    val addQueueItem: AddQueueItem,
    val removeQueueItem: RemoveQueueItem,
    val clearQueue: ClearQueue,
    val queueSize: QueueSize,
    val isEmpty: IsEmpty,
    val isEqual: IsEqual,
    val peekQueue: PeekQueue
)
class SongQueueException(
    message: String = "",
    val errorCode: Int = 0
) : Exception(message)