package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData

class RemoveQueueItem(private var _songQueueRepository: SongQueueRepository) {
    operator fun invoke(song: AudioFileMetaData) : Boolean {
        return _songQueueRepository.remove(song)
    }
}