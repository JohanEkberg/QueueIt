package se.johan.queueit.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.johan.queueit.TAG
import se.johan.queueit.mediastore.ScanHandler
import se.johan.queueit.model.usecases.AudioDataUseCases
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases,
    private val scanHandler: ScanHandler
) : ViewModel() {
    private val _pendingScan: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var pendingScan: StateFlow<Boolean> = _pendingScan

    val artistsDetected: StateFlow<List<String>> = scanHandler.artistsDetected
    val successfulScan: StateFlow<Boolean?> = scanHandler.successfulScan

    fun doScan(context: Context) {
        viewModelScope.launch {
            _pendingScan.value = true
            withContext(Dispatchers.IO) {
                try {
                    if (audioDataUseCases.databaseExist(context)) {
                        audioDataUseCases.clearAllDatabaseTables()
                    }
                    scanHandler.startScan(context)
                } catch (e: Exception) {
                    Log.e("SettingsViewModel", "Clearing DB failed: ${e.message}")
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