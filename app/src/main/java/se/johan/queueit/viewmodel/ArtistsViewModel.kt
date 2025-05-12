package se.johan.queueit.viewmodel

import android.util.Log
import androidx.compose.ui.unit.Dp
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
import se.johan.queueit.model.database.ArtistWithSongs
import se.johan.queueit.model.database.SongEntity
import se.johan.queueit.model.usecases.AudioDataUseCases
import se.johan.queueit.util.calculateGridImageSize
import se.johan.queueit.util.calculateListImageSize
import javax.inject.Inject

@HiltViewModel
class ArtistsViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases
) : ViewModel() {
    private val _artists = MutableStateFlow<PagingData<ArtistWithSongs>>(PagingData.empty())
    val artists: StateFlow<PagingData<ArtistWithSongs>> = _artists

    fun getArtists() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    audioDataUseCases.getPagedArtistWithSongs()
                        .cachedIn(this) // still cache paging flow inside this coroutine
                        .collect { pagingData ->
                            _artists.value = pagingData
                        }
                } catch(e: Exception) {
                    Log.e(TAG, "Failed to get albums, exception: ${e.message}")
                }
            }
        }
    }

    fun getNumberOfAlbums(listOfSongs: List<SongEntity>) : String {
        return try {
            listOfSongs
                .mapNotNull { it.songAlbumId }
                .distinct()
                .count().toString()
        } catch(e: Exception) {
            Log.e(TAG, "Failed to get number of albums, exception: ${e.message}")
            ""
        }
    }

    fun getItemSize(screenWidthDp: Int): Dp = calculateListImageSize(screenWidthDp)
}