package se.johan.queueit.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.johan.queueit.TAG
import se.johan.queueit.audio.data.AudioFileMetaData
import se.johan.queueit.audio.queue.SongQueueUseCases
import se.johan.queueit.mediastore.util.getAlbumArtWork
import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.AlbumWithSongs2
import se.johan.queueit.model.database.ArtistEntity
import se.johan.queueit.model.database.SongEntity
import se.johan.queueit.model.database.SongWithArtist
import se.johan.queueit.model.usecases.AudioDataUseCases
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases,
    private val songQueue: SongQueueUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _albumsFromArtist: MutableState<List<AlbumUiModel>> = mutableStateOf(
        listOf(
            AlbumUiModel(
                artist = ArtistEntity(artistId = 0, artistName = ""),
                album = AlbumEntity(albumName = "", albumUri = "", year = "", genre = ""),
                songs = emptyList()
            )
        )
    )

    val albumsFromArtist: State<List<AlbumUiModel>> = _albumsFromArtist

    init {
        val artistId = savedStateHandle.get<Long>("artistId") ?: -1
        getAlbumsFromArtistId(artistId)
    }

    private fun getAlbumsFromArtistId(artistId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val artist = audioDataUseCases.getArtistWithAlbums(artistId)
                    _albumsFromArtist.value = groupSongsByAlbum(
                        artist.artistEntity,
                        artist.songList
                    )
                } catch(e: Exception) {
                    Log.e(TAG, "Failed to get album, exception: ${e.message}")
                }
            }
        }
    }

    private fun groupSongsByAlbum(artist: ArtistEntity, data: List<AlbumWithSongs2>): List<AlbumUiModel> {
        return data
            .filter { it.album != null }
            .groupBy { it.album!! } // group by album
            .map { (album, items) ->
                AlbumUiModel(
                    artist = artist,
                    album = album,
                    songs = items.map { it.songEntity },
                    isExpanded = false
                )
            }
    }

    val addTrackToQueue: ((SongWithArtist) -> Unit) = { addSongToQueue(it) }
    private fun addSongToQueue(song: SongWithArtist) {
        songQueue.addQueueItem(
            AudioFileMetaData(
                songUri = Uri.parse(song.songEntity.songUri),
                album = "",
                title = song.songEntity.songName ?: "",
                artist = song.artist?.artistName ?: "",
                genre = "",
                year = "",
                format = song.songEntity.format ?: "",
                duration = song.songEntity.duration ?: "",
                resolution = song.songEntity.resolution ?: "",
                size = song.songEntity.size ?: 0,
                bitmap = null,
                albumUri = Uri.parse(song.songEntity.albumUri)
            )
        )
    }
}

data class AlbumUiModel(
    val artist: ArtistEntity,
    val album: AlbumEntity,
    val songs: List<SongEntity>,
    var isExpanded: Boolean = false // UI state
)