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
import se.johan.queueit.util.calculateListImageSize
import javax.inject.Inject

@HiltViewModel
class ArtistsPageViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases
) : ViewModel() {

    // Automatically starts collecting and caching PagingData
    val artists = audioDataUseCases
        .getPagedArtistWithSongs()
        .cachedIn(viewModelScope)


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