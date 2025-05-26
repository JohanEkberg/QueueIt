package se.johan.queueit.audio.queue

class QueueSize(private var _songQueueRepository: SongQueueRepository) {
    operator fun invoke() : Int {
        return _songQueueRepository.getSize()
    }
}