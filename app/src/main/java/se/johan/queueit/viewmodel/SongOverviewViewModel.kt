package se.johan.queueit.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.johan.queueit.TAG
import se.johan.queueit.apiservices.ApiServicesUseCases
import se.johan.queueit.apiservices.ArtistInfo
import se.johan.queueit.apiservices.LyricsApiResult
import se.johan.queueit.apiservices.ArtistOverviewApiResult
import se.johan.queueit.ui.UiEvent
import javax.inject.Inject

@HiltViewModel
class SongOverviewViewModel @Inject constructor (
    private val apiServices: ApiServicesUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _lyric = MutableStateFlow("")
    val lyric: StateFlow<String> = _lyric.asStateFlow()

    private val _artist = MutableStateFlow(ArtistInfo())
    val artist: StateFlow<ArtistInfo> = _artist.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        val artist = savedStateHandle.get<String>("artist") ?: ""
        val title = savedStateHandle.get<String>("title") ?: ""
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    when (val result = apiServices.getLyric(artist = artist, title = title)) {
                        is LyricsApiResult.Success -> {
                            _lyric.value = result.lyric
                        }
                        is LyricsApiResult.Error -> {
                            Log.w(TAG, "No lyric was found, exception: ${result.message}, error: ${result.errorCode}")
                            _uiEvent.emit(UiEvent.ShowSnackbar(result.message))
                        }
                    }
                } catch(e: Exception) {
                    Log.e(TAG, "Failed to get the lyric, exception: ${e.message}")
                    _uiEvent.emit(UiEvent.ShowSnackbar(e.message ?: "No lyric was found"))
                }
            }
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    when (val result = apiServices.getArtistOverview(artist = artist)) {
                        is ArtistOverviewApiResult.Success -> {
                            _artist.value = result.artistInfo
                            // show data
                        }
                        is ArtistOverviewApiResult.Error -> {
                            Log.w(TAG, "No artist was found, exception: ${result.message}, error: ${result.errorCode}")
                            _uiEvent.emit(UiEvent.ShowSnackbar(result.message))
                        }
                    }
                } catch(e: Exception) {
                    Log.e(TAG, "Failed to get the artist overview, exception: ${e.message}")
                    _uiEvent.emit(UiEvent.ShowSnackbar(e.message ?: "No artist was found"))
                }
            }
        }
    }
}