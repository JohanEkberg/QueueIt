package se.johan.queueit.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.johan.queueit.TAG
import se.johan.queueit.model.database.AlbumWithSongs
import se.johan.queueit.model.usecases.AudioDataUseCases
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases
) : ViewModel() {
    // Backing field with MutableStateFlow
    private val _albums = MutableStateFlow<PagingData<AlbumWithSongs>>(PagingData.empty())
    val albums: StateFlow<PagingData<AlbumWithSongs>> = _albums

    fun getAlbums() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    audioDataUseCases.getPagedAlbumWithSong()
                        .cachedIn(this) // still cache paging flow inside this coroutine
                        .collect { pagingData ->
                            _albums.value = pagingData
                        }
                } catch(e: Exception) {
                    Log.e(TAG, "Failed to get albums, exception: ${e.message}")
                }
            }
        }
    }
}