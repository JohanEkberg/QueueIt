package se.johan.queueit.model.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import se.johan.queueit.model.database.AlbumWithSongs
import se.johan.queueit.model.database.AudioDataDao

class GetPagedAlbumWithSong(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(): Flow<PagingData<AlbumWithSongs>> {
        return try {
            Pager(
                PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true,
                    maxSize = 200)
            ) {
                dao.getPagedAlbumWithSongs()
            }.flow
        } catch(e: Exception) {
            throw AudioDataException("Failed to get album with songs data, exception: ${e.message}")
        }
    }
}