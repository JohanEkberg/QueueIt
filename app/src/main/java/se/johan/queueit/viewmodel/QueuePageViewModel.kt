package se.johan.queueit.viewmodel

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import se.johan.queueit.TAG
import se.johan.queueit.audio.data.AudioFileMetaData
import se.johan.queueit.audio.queue.SongQueueUseCases
import se.johan.queueit.util.calculateListImageSize
import javax.inject.Inject

@HiltViewModel
class QueuePageViewModel @Inject constructor (
    private val songQueueUseCases: SongQueueUseCases
) : ViewModel() {

    val queueItems: StateFlow<List<AudioFileMetaData>> = songQueueUseCases
        .observeQueue()
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getItemSize(screenWidthDp: Int): Dp = calculateListImageSize(screenWidthDp, min = 100, max = 140)

    val removeQueueItem: (AudioFileMetaData) -> Unit = { removeItemFromQueue(it) }
    private fun removeItemFromQueue(song: AudioFileMetaData) {
        try {
            songQueueUseCases.removeQueueItem(song)
        } catch(e: Exception) {
            Log.e(TAG, "Failed to remove song from queue, exception ${e.message}")
        }
    }
}