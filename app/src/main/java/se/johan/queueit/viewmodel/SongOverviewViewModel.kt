package se.johan.queueit.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import se.johan.queueit.TAG
import se.johan.queueit.apiservices.ApiServicesException
import se.johan.queueit.apiservices.ApiServicesUseCases
import se.johan.queueit.ui.UiEvent
import javax.inject.Inject

@HiltViewModel
class SongOverviewViewModel @Inject constructor (
    private val apiServices: ApiServicesUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _lyric = MutableStateFlow("")
    val lyric: StateFlow<String> = _lyric.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        val artist = savedStateHandle.get<String>("artist") ?: ""
        val title = savedStateHandle.get<String>("title") ?: ""
        viewModelScope.launch {
            try {
                //_lyric.value = repository.getLyric(artist = artist, title = title).lyrics
                _lyric.value = apiServices.getLyric(artist = artist, title = title).lyrics
            } catch (e: ApiServicesException) {
                Log.w(TAG, "No lyric was found, exception: ${e.message}, error: ${e.errorCode}")
                _uiEvent.emit(UiEvent.ShowSnackbar(e.message ?: "No lyric was found"))
            } catch(e: Exception) {
                Log.e(TAG, "Failed to get the lyric, exception: ${e.message}")
            }
        }
    }
}