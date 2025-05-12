package se.johan.queueit.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import se.johan.queueit.TAG
import se.johan.queueit.audio.player.MusicPlayerUseCases
import se.johan.queueit.audio.queue.SongQueueUseCases
import se.johan.queueit.mediastore.util.getAlbumArtWork
import se.johan.queueit.ui.screens.components.Player
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val musicPlayer: MusicPlayerUseCases,
    private val songQueue: SongQueueUseCases
) : ViewModel() {
    var content by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    var currentSong by mutableStateOf<SongData?>(null)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    var isVisible: Boolean = false
        private set

    var playerHeight: Dp = 0.dp
        private set

    private fun setCurrentSong(context: Context) {
        val currentTrack = songQueue.getQueueItem()
        currentTrack?.let { song ->
            val artwork = getAlbumArtWork(context, song.albumUri)
            currentSong = SongData(
                artwork = artwork,
                artist = song.artist,
                title = song.title
            )
        } ?: run {
            onStop()
            hide()
        }
    }

    fun updatePlayerHeight(height: Dp) {
        playerHeight = height
    }

    fun togglePlayPause(context: Context) {
        if (isPlaying) {
            onPause()
        } else {
            onPlay(context)
        }
        isPlaying = !isPlaying
    }

    fun show(context: Context, content: @Composable () -> Unit) {
        setCurrentSong(context)
        this.content = content
        isVisible = true
    }

    fun hide() {
        this.content = null
        this.currentSong = null
        isVisible = false
    }

    fun onPlay(context: Context) {
        try {
            musicPlayer.play(context)
        } catch(e: Exception) {
            Log.e(TAG, "Play failed, exception ${e.message}")
        }

    }

    fun onStop() {
        try {
            musicPlayer.stop()
        } catch(e: Exception) {
            Log.e(TAG, "Stop failed, exception ${e.message}")
        }
    }

    private fun onPause() {
        try {
            musicPlayer.pause()
        } catch(e: Exception) {
            Log.e(TAG, "Pause failed, exception ${e.message}")
        }
    }

    fun onSkip(context: Context) {
        try {
            musicPlayer.skip(context)
            setCurrentSong(context)
        } catch(e: Exception) {
            Log.e(TAG, "Skip failed, exception ${e.message}")
        }
    }
}

fun BottomSheetViewModel.dynamicBottomPadding() = if (isVisible) {
    if(playerHeight > 0.dp) {playerHeight} else {64.dp}
} else {
    0.dp
}

data class SongData(
    val artwork: Bitmap,
    val artist: String,
    val title: String
)