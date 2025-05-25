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
import se.johan.queueit.model.database.AlbumWithSongs
import se.johan.queueit.model.usecases.AudioDataUseCases
import se.johan.queueit.util.calculateGridImageSize
import javax.inject.Inject

@HiltViewModel
class AlbumsPageViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases
) : ViewModel() {

    // Automatically starts collecting and caching PagingData
    val albums = audioDataUseCases
        .getPagedAlbumWithSong()
        .cachedIn(viewModelScope)

    fun getItemSize(screenWidthDp: Int): Dp = calculateGridImageSize(screenWidthDp)
}