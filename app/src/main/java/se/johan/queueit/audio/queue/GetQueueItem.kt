package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData

class GetQueueItem(private var _songQueueRepository: SongQueueRepository) {
    operator fun invoke(): AudioFileMetaData? {
        return _songQueueRepository.getItem()
    }
}