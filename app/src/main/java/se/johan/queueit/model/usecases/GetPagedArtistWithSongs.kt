package se.johan.queueit.model.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import se.johan.queueit.model.database.ArtistWithSongs
import se.johan.queueit.model.database.AudioDataDao

class GetPagedArtistWithSongs(private val dao: AudioDataDao) {
    @Throws(AudioDataException::class)
    operator fun invoke(): Flow<PagingData<ArtistWithSongs>> {
        return try {
            Pager(
                PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true,
                    maxSize = 200)
            ) {
                dao.getPagedArtistsWithSongs()
            }.flow
        } catch(e: Exception) {
            throw AudioDataException("Failed to get artist with songs data, exception: ${e.message}")
        }
    }
}