package se.johan.queueit.viewmodel

import android.graphics.Bitmap
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
import se.johan.queueit.mediastore.util.getAlbumArtWork
import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.AlbumWithSongs2
import se.johan.queueit.model.database.SongEntity
import se.johan.queueit.model.usecases.AudioDataUseCases
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _albumsFromArtist: MutableState<List<AlbumUiModel>> = mutableStateOf(
        listOf(
            AlbumUiModel(
                artist = "",
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
                        artist.artistEntity.artistName ?: "",
                        artist.songList
                    )
                } catch(e: Exception) {
                    Log.e(TAG, "Failed to get album, exception: ${e.message}")
                }
            }
        }
    }

    private fun groupSongsByAlbum(artist: String, data: List<AlbumWithSongs2>): List<AlbumUiModel> {
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
}

data class AlbumUiModel(
    val artist: String,
    val album: AlbumEntity,
    val songs: List<SongEntity>,
    var isExpanded: Boolean = false // UI state
)