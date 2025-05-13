package se.johan.queueit.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import se.johan.queueit.model.usecases.AudioDataUseCases
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases,
    private val audioScannerUseCases: AudioScannerUseCases
) : ViewModel() {
    private val _pendingScan: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var pendingScan: StateFlow<Boolean> = _pendingScan

    private val _successfulScan: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var successfulScan: StateFlow<Boolean> = _successfulScan

    fun doScan(context: Context) {
        viewModelScope.launch {
            _pendingScan.value = true
            _successfulScan.value = withContext(Dispatchers.IO) {
                try {
                    if (audioDataUseCases.databaseExist(context)) {
                        audioDataUseCases.clearAllDatabaseTables()
                    }
                    audioScannerUseCases.startScan(context)
                } catch (e: Exception) {
                    Log.e(TAG, "Scan autio data failed, ${e.message}")
                    false
                }
            }
            _pendingScan.value = false
        }
    }

//    fun getArtistsArtwork(context: Context) {
//        viewModelScope.launch {
//            _successfulScan.value = withContext(Dispatchers.IO) {
//                try {
//                   true
//                } catch (e: Exception) {
//                    Log.e(TAG, "Scan autio data failed, ${e.message}")
//                    false
//                }
//            }
//        }
//    }
}