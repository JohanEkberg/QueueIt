package se.johan.queueit.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.johan.queueit.TAG
import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.ArtistEntity
import se.johan.queueit.model.database.SongEntity
import se.johan.queueit.model.usecases.AudioDataUseCases
import javax.inject.Inject

@HiltViewModel
class SharedSearchViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases
) : ViewModel() {

    private val _artists = MutableStateFlow<List<ArtistEntity>>(emptyList())
    val artists: StateFlow<List<ArtistEntity>> = _artists

    private val _albums = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val albums: StateFlow<List<AlbumEntity>> = _albums

    private val _songs = MutableStateFlow<List<SongEntity>>(emptyList())
    val songs: StateFlow<List<SongEntity>> = _songs

    fun doSearch(searchString: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _artists.value = audioDataUseCases.getArtistByName("$searchString%")
                    _albums.value = audioDataUseCases.getAlbumByName("$searchString%")
                    _songs.value = audioDataUseCases.getSongByName("$searchString%")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to get artists, exception ${e.message}")
                }
            }
        }
    }
}