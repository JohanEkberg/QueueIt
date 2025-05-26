package se.johan.queueit.audio.queue

import se.johan.queueit.audio.data.AudioFileMetaData

class IsEqual(private var _songQueueRepository: SongQueueRepository) {
    operator fun invoke(songQueue: List<AudioFileMetaData>) : Boolean {
        return _songQueueRepository.isEqual(songQueue)
    }
}