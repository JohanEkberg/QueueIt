package se.johan.queueit.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import se.johan.queueit.TAG
import se.johan.queueit.audio.data.AudioFileMetaData
import se.johan.queueit.audio.queue.SongQueueUseCases
import se.johan.queueit.model.database.SongWithArtist
import se.johan.queueit.util.calculateListImageSize
import javax.inject.Inject

@HiltViewModel
class QueuePageViewModel @Inject constructor (
    private val songQueueUseCases: SongQueueUseCases
) : ViewModel() {

    private val _queueItems = MutableStateFlow<List<AudioFileMetaData>>(emptyList())
    val queueItems: StateFlow<List<AudioFileMetaData>> = _queueItems

    fun getQueueItems() {
        try {
            _queueItems.value = songQueueUseCases.getQueueItems()
        } catch(e: Exception) {
            Log.e(TAG, "Failed to get song queue items, exception ${e.message}")
        }
    }

    fun getItemSize(screenWidthDp: Int): Dp = calculateListImageSize(screenWidthDp, min = 100, max = 140)

    val removeQueueItem: (AudioFileMetaData) -> Unit = { removeItemFromQueue(it) }
    private fun removeItemFromQueue(song: AudioFileMetaData) {
        try {
            songQueueUseCases.removeQueueItem(song)
            getQueueItems()
        } catch(e: Exception) {
            Log.e(TAG, "Failed to remove song from queue, exception ${e.message}")
        }
    }
}