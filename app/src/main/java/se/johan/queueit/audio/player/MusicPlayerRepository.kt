package se.johan.queueit.audio.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import se.johan.queueit.TAG
import se.johan.queueit.audio.data.AudioFileMetaData
import se.johan.queueit.audio.queue.SongQueueUseCases
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicPlayerRepository @Inject constructor(
    private val songQueueUseCases: SongQueueUseCases
) {
    private var _mediaPlayer: MediaPlayer? = null
    private val _handler = Handler(Looper.getMainLooper())
    private var _songChangeCallback: ((AudioFileMetaData?) -> Unit)? = null
    private var _progressCallback: ((Int) -> Unit)? = null
    private var _currentSong: AudioFileMetaData? = null
    private var _initDone = false

    @Synchronized
    private fun getMediaPlayerInstance() : MediaPlayer? {
        return if (_mediaPlayer != null) {
            _mediaPlayer
        } else {
            _mediaPlayer = MediaPlayer()
            _mediaPlayer
        }
    }

    private fun setupAudioErrorCallback() {
        try {
            getMediaPlayerInstance()?.setOnErrorListener { _, _, _ ->
                Log.i(TAG, "Player error callback")
                return@setOnErrorListener true
            }
        } catch(e: Exception) {
            Log.e(TAG, "Completion failed, exception: ${e.message}")
        }
    }

    private fun setupAudioCompletionCallback(context: Context) {
        try {
            // Song has completed callback
            getMediaPlayerInstance()?.setOnCompletionListener {
                Log.i(TAG, "Song finished callback")
                _currentSong = null
                _progressCallback?.let { it(100) }
                playSongFromQueue(context)
            }
        } catch(e: Exception) {
            Log.e(TAG, "Completion failed, exception: ${e.message}")
        }
    }

    private fun clearMediaPlayer() {
        _initDone = false
        getMediaPlayerInstance()?.release()
        _mediaPlayer = null
        _currentSong = null
        _songChangeCallback?.let { it(null) }
    }

    fun play(context: Context) {
        try {
            if (!_initDone) {
                setupAudioErrorCallback()
                setupAudioCompletionCallback(context)
                _initDone = true
                playSongFromQueue(context)
            } else {
                Log.i(TAG, "Resume current song")
                getMediaPlayerInstance()?.start()
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }


    private fun playSongFromQueue(context: Context) {
        try {
            if (!songQueueUseCases.isEmpty()) {
                Log.i(TAG, "Play from queue")
                _currentSong = songQueueUseCases.getQueueItem()
                Log.i(TAG, "New song to play: ${_currentSong?.artist} - ${_currentSong?.title}")
                _currentSong?.songUri?.let { songNotNull ->
                    if (playSong(context, songNotNull)) {
                        _songChangeCallback?.let { it(_currentSong) }
                    }
                }
            } else if (_currentSong != null) {
                Log.i(TAG, "Queue is empty, play current song")
                _currentSong?.songUri?.let { songNotNull ->
                    if (playSong(context, songNotNull)) {
                        _songChangeCallback?.let { it(_currentSong) }
                    }
                }
            } else {
                Log.w(TAG, "Queue is empty!")
                clearMediaPlayer()
            }
        } catch(e: Exception) {
            Log.e(TAG, "Completion failed, exception: ${e.message}")
        }
    }

    private fun playSong(context: Context, songUri: Uri) : Boolean {
        var success = false
        try {
            //Log.i(TAG, "Play new song ${songUri}")
            getMediaPlayerInstance()?.apply {
                reset()
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(context, songUri)
                prepare()
                start()
            } ?: Log.e(TAG, "Failed to start player!")
            _handler.postDelayed(UpdateSongTime,100)
            success = true
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return success
    }

    private val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            if (_initDone) {
                val currentPosition = getMediaPlayerInstance()?.currentPosition?.toFloat() ?: 0F
                val duration = getMediaPlayerInstance()?.duration?.toFloat() ?: 0F
                val progress: Float = (currentPosition / duration) * 100

                _progressCallback?.let { it(progress.toInt()) }
                if (progress < 100) {
                    _handler.postDelayed(this, 100)
                }
            } else {
                _progressCallback?.let { it(0) }
            }
        }
    }

    fun pause() {
        try {
            Log.i(TAG, "Pause current song")
            getMediaPlayerInstance()?.pause()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun skip(context: Context) {
        try {
            Log.i(TAG, "Stop current song from playing")
            val player = getMediaPlayerInstance()
            val isPlaying = player?.isPlaying == true
            player?.stop()
            if (isPlaying && songQueueUseCases.queueSize() > 0) {
                playSongFromQueue(context)
            } else {
                // Remove one item from the queue,
                // if the queue is empty report empty queue.
                _initDone = false
                _currentSong = songQueueUseCases.getQueueItem()
                if (_currentSong == null && songQueueUseCases.queueSize() == 0) {
                    Log.w(TAG, "Queue is empty!")
                    _songChangeCallback?.let { it(null) }
                } else {
                    _songChangeCallback?.let { it(_currentSong) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        try {
            _handler.removeCallbacks(UpdateSongTime)
            getMediaPlayerInstance()?.stop()
            clearMediaPlayer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}