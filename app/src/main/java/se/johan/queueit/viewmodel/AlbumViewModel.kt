package se.johan.queueit.viewmodel

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
import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.AlbumWithSongs
import se.johan.queueit.model.database.SongWithArtist
import se.johan.queueit.model.usecases.AudioDataUseCases
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases,
    private val songQueue: SongQueueUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _album: MutableState<AlbumWithSongs>  = mutableStateOf(
        AlbumWithSongs(AlbumEntity(albumName = "", albumUri = "", year = "", genre = ""), emptyList())
    )
    val album: State<AlbumWithSongs> = _album

    init {
        val albumId = savedStateHandle.get<Long>("albumId") ?: -1
        getAlbumSongs(albumId)
    }

    private fun getAlbumSongs(albumId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _album.value = audioDataUseCases.getAlbumSongs(albumId)
                    Log.i(TAG, "Album retrieved: ${_album.value.albumEntity.albumId}")
                } catch(e: Exception) {
                    Log.e(TAG, "Failed to get album, exception: ${e.message}")
                }
            }
        }
    }

    val addTrackToQueue: ((SongWithArtist) -> Unit) = { addSongToQueue(it) }
    private fun addSongToQueue(song: SongWithArtist) {
        songQueue.addQueueItem(
            AudioFileMetaData(
                songUri = Uri.parse(song.songEntity.songUri),
                album = "", // TODO: Is this needed?
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