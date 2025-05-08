package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.AudioDataDao

class DeleteAllData(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke() {
        return try {
            dao.deleteAllData()
        } catch(e: Exception) {
            throw AudioDataException("Failed to delete all data, exception: ${e.message}")
        }
    }
}