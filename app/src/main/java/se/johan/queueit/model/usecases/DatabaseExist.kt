package se.johan.queueit.model.usecases

import android.content.Context
import se.johan.queueit.model.database.AudioDatabase

class DatabaseExist {
    operator fun invoke(context: Context) : Boolean {
        return AudioDatabase.isValidFile(context)
    }
}