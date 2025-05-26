package se.johan.queueit.audio.queue

class ClearQueue(private var _songQueueRepository: SongQueueRepository) {
    operator fun invoke() : Boolean {
        return _songQueueRepository.clear()
    }
}