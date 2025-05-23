package se.johan.queueit.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.johan.queueit.TAG
import se.johan.queueit.audio.data.AudioFileMetaData
import se.johan.queueit.audio.queue.SongQueueUseCases
import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.ArtistEntity
import se.johan.queueit.model.database.SongEntity
import se.johan.queueit.model.database.SongWithArtist
import se.johan.queueit.model.usecases.AudioDataUseCases
import javax.inject.Inject

@HiltViewModel
class SharedSearchViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases,
    private val songQueue: SongQueueUseCases
) : ViewModel() {

    private val _artists = MutableStateFlow<List<ArtistEntity>>(emptyList())
    val artists: StateFlow<List<ArtistEntity>> = _artists

    private val _albums = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val albums: StateFlow<List<AlbumEntity>> = _albums

    private val _songs = MutableStateFlow<List<SongEntity>>(emptyList())
    val songs: StateFlow<List<SongEntity>> = _songs

    private val _songWithArtist = Channel<SongWithArtist>()
    val songWithArtist = _songWithArtist.receiveAsFlow()

    fun doSearch(searchString: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _artists.value = audioDataUseCases.getArtistByName("$searchString%")
                    _albums.value = audioDataUseCases.getAlbumByName("$searchString%")
                    _songs.value = audioDataUseCases.getSongByName("$searchString%")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to get artists, albums and songs, exception ${e.message}")
                }
            }
        }
    }

    fun onSongClicked(song: SongEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val artist = audioDataUseCases.getArtistById(song.songArtistId?.toInt() ?: 0)
                    val artistEntity = ArtistEntity(
                        artistId = artist.artistId,
                        artistName = artist.artistName ?: ""
                    )
                    _songWithArtist.send(
                        SongWithArtist(songEntity = song, artist = artistEntity)
                    )
                } catch(e: Exception) {
                    Log.e(TAG, "Failed to get artist, exception ${e.message}")
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