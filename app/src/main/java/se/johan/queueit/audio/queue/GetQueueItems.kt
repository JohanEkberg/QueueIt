package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData

class GetQueueItems(private var _songQueueRepository: SongQueueRepository) {
    operator fun invoke(): List<AudioFileMetaData> {
        return _songQueueRepository.getItems()
    }
}