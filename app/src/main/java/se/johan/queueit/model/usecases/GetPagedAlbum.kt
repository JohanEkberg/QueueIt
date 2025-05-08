package se.johan.queueit.model.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.AudioDataDao

class GetPagedAlbum(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(): Flow<PagingData<AlbumEntity>> {
        return try {
            Pager(
                PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true,
                    maxSize = 200)
            ) {
                dao.getPagedAlbums()
            }.flow
        } catch(e: Exception) {
            throw AudioDataException("Failed to get album data, exception: ${e.message}")
        }
    }
}