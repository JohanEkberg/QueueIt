package se.johan.queueit.audio.queue

import kotlinx.coroutines.flow.Flow
import se.johan.queueit.audio.data.AudioFileMetaData

class ObserveQueue(private var _songQueueRepository: SongQueueRepository) {
    operator fun invoke() : Flow<List<AudioFileMetaData>> {
        return _songQueueRepository.observe()
    }
}