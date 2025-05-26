package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData

class PeekQueue(private var _songQueueRepository: SongQueueRepository) {
    operator fun invoke(): AudioFileMetaData? {
        return _songQueueRepository.peek()
    }
}