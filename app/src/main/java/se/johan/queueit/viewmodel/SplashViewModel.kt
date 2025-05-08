package se.johan.queueit.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.johan.queueit.TAG
import se.johan.queueit.mediastore.usecases.AudioScannerUseCases
import se.johan.queueit.mediastore.usecases.StartScan
import se.johan.queueit.model.database.AudioDatabase
import se.johan.queueit.model.usecases.AddAlbum
import se.johan.queueit.model.usecases.AddArtist
import se.johan.queueit.model.usecases.AddSong
import se.johan.queueit.model.usecases.AudioDataUseCases
import se.johan.queueit.model.usecases.DatabaseExist
import se.johan.queueit.model.usecases.GetSongs
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases,
    private val audioScannerUseCases: AudioScannerUseCases
) : ViewModel() {

//    private val _requireScan: MutableState<Boolean?> = mutableStateOf(null)
//    var requireScan: State<Boolean?> = _requireScan

    private val _successfulStartup: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    var successfulStartup: StateFlow<Boolean?> = _successfulStartup

//    private val _isPermissionGranted: MutableState<Boolean> = mutableStateOf(false)
//    val isPermissionGranted: State<Boolean> = _isPermissionGranted

    private val _isPermissionGranted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPermissionGranted: StateFlow<Boolean> = _isPermissionGranted

    fun updatePermissionStatus(granted: Boolean) {
        if (_isPermissionGranted.value != granted) {
            Log.i(TAG, "Permission granted: $granted")
            _isPermissionGranted.value = granted
        }
    }

    fun requireScan(context: Context) {
        viewModelScope.launch {
            _successfulStartup.value = withContext(Dispatchers.IO) {
                try {
                    if (audioDataUseCases.databaseExist(context)) {
                        true
                    } else {
                        audioScannerUseCases.startScan(context)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Scan autio data failed, ${e.message}")
                    false
                }
            }
        }
    }
}