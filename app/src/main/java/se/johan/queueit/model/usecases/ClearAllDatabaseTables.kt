package se.johan.queueit.model.usecases

import se.johan.queueit.model.database.AudioDatabase

class ClearAllDatabaseTables(private val db: AudioDatabase) {
    @Throws(AudioDataException::class)
    operator fun invoke() {
        return try {
            db.clearAllTables()
        } catch(e: Exception) {
            throw AudioDataException("Failed to clear all database tables, exception: ${e.message}")
        }
    }
}