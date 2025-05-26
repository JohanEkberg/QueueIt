package se.johan.queueit.audio.queue

class IsEmpty(private var _songQueueRepository: SongQueueRepository) {
    operator fun invoke() : Boolean {
        return _songQueueRepository.isEmpty()
    }
}